package com.ms.crud_api.controller.backend;

import com.ms.crud_api.exception.BadRequestException;
import com.ms.crud_api.exception.NotFoundException;
import com.ms.crud_api.infrastructure.model.response.BaseResponse;
import com.ms.crud_api.infrastructure.model.response.body.BaseBodyResponse;
import com.ms.crud_api.infrastructure.model.response.error.ErrorResponse;
import com.ms.crud_api.model.entity.PaymentMethodEntity;
import com.ms.crud_api.model.entity.UserEntity;
import com.ms.crud_api.model.request.payment.PaymentRequest;
import com.ms.crud_api.model.request.payment.PaymentRestore;
import com.ms.crud_api.model.request.user.UserRequest;
import com.ms.crud_api.model.request.user.UserRestoreRequest;
import com.ms.crud_api.model.response.Payment.PaymentResponse;
import com.ms.crud_api.model.response.user.UserResponse;
import com.ms.crud_api.service.PaymentService;
import com.ms.crud_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Tag(name = "Payment", description = "Payment Management")
@RestController
@RequestMapping("/api/payment")
public class PaymentController {


    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @Operation(summary = "Endpoint for admin create a Payment", description = "Admin can creating a Payment by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "201", content = @Content(schema = @Schema(implementation = PaymentResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @PostMapping
    public ResponseEntity<BaseBodyResponse> create(@Valid @RequestBody PaymentRequest request) throws Exception {
        PaymentMethodEntity paymentMethodEntity = this.paymentService.create(request);

        return BaseBodyResponse.createSuccess(PaymentResponse.fromEntity(paymentMethodEntity), "Created Successfully");
    }

    @Operation(summary = "Endpoint for admin update a Payment", description = "Admin can updating a Payment by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = PaymentResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @PutMapping("/{id}")
    public ResponseEntity<BaseBodyResponse> update(@PathVariable Long id, @Valid @RequestBody PaymentRequest request) throws Exception {
        PaymentMethodEntity paymentMethodEntity = this.paymentService.update((id), request);

        return BaseBodyResponse.success(PaymentResponse.fromEntity(paymentMethodEntity), "Updated Successfully");
    }

    @Operation(summary = "Endpoint for admin find all Payment",
            description = "Admin can find all Payment by using this endpoint",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PaymentResponse.class), mediaType = "application/json")),
                    @ApiResponse(description = "Error", responseCode = "400-500",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
            })
    @GetMapping
    public ResponseEntity<BaseBodyResponse> findAll(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "isPage", required = false, defaultValue = "true") boolean isPage,
            @RequestParam(name = "sort", required = false, defaultValue = "id:desc") String sort,
            @RequestParam(name = "isTrash", required = false, defaultValue = "false") boolean isTrash,
            @RequestParam Map<String, String> reqParam) throws BadRequestException {

        // Validate page and limit parameters
        if (page < 1 || limit < 1) {
            throw new BadRequestException("Page and limit must be greater than 0.");
        }

        // Fetch flights and map to FlightResponse
        Page<BaseResponse> paymentMethodEntity = this.paymentService
                .findAll(page, limit, isPage, sort, isTrash, reqParam)
                .map(PaymentResponse::fromEntity);

        // Return a successful response
        return BaseBodyResponse.success(paymentMethodEntity, "Fetched users successfully.");
    }


    @Operation(summary = "Endpoint for admin find Payment",
            description = "Admin can find Payment by using this endpoint",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PaymentResponse.class), mediaType = "application/json")),
                    @ApiResponse(description = "Error", responseCode = "400-500",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
            })
    @GetMapping("/{id}")
    public ResponseEntity<BaseBodyResponse> findOne(@PathVariable Long id) throws NotFoundException {
        PaymentMethodEntity paymentMethodEntity = this.paymentService.findOne(id);

        return BaseBodyResponse.success(PaymentResponse.fromEntity(paymentMethodEntity), "Found Successfully");
    }

    @Operation(summary = "Endpoint for admin delete a Payment", description = "Admin can delete a Payment by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = PaymentResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseBodyResponse> delete(@PathVariable Long id) throws Exception {
        PaymentMethodEntity paymentMethodEntity = this.paymentService.delete(id);

        return BaseBodyResponse.success(PaymentResponse.fromEntity(paymentMethodEntity), "Deleted Successfully");
    }

    @Operation(summary = "Endpoint for admin restore a Payment", description = "Admin can restore a Payment by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = PaymentResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})

    @PutMapping("/restore/{id}")
    public ResponseEntity<BaseBodyResponse> restore(@PathVariable Long id, @RequestBody PaymentRestore req) throws Exception {
        PaymentMethodEntity paymentMethodEntity = this.paymentService.restore(id,req);

        return BaseBodyResponse.success(PaymentResponse.fromEntity(paymentMethodEntity), "Restored Successfully");
    }

    @Operation(summary = "Endpoint for admin find a Payment from trash", description = "Admin can find a Payment from trash by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = PaymentResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @GetMapping("/find-trash/{id}")
    public ResponseEntity<BaseBodyResponse>findOneWithSoftDeleted(@PathVariable Long id) throws NotFoundException{
        PaymentMethodEntity paymentMethodEntity = this.paymentService.findOneWithSoftDeleted(id);
        return BaseBodyResponse.success(PaymentResponse.fromEntity(paymentMethodEntity), "Found Successfully");
    }
    @Operation(summary = "Endpoint for admin delete a Payment from trash", description = "Admin can delete a Payment from trash by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = PaymentResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @DeleteMapping("/delete-trash/{id}")
    public ResponseEntity<BaseBodyResponse>deleteSoftDeleted(@PathVariable Long id) throws Exception{
        PaymentMethodEntity paymentMethodEntity = this.paymentService.deleteFromTrash(id);
        return BaseBodyResponse.success(PaymentResponse.fromEntity(paymentMethodEntity), "Deleted Successfully");
    }


    @Operation(summary = "Endpoint for admin delete a Payment", description = "Admin can delete a Payment by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = PaymentResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @DeleteMapping("/force-delete/{id}")
    public ResponseEntity<BaseBodyResponse>forceDelete(@PathVariable Long id) throws Exception{
        PaymentMethodEntity paymentMethodEntity = this.paymentService.ForceDelete(id);
        return BaseBodyResponse.success(PaymentResponse.fromEntity(paymentMethodEntity), "Deleted Successfully");
    }


}
