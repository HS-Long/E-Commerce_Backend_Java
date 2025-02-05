package com.ms.crud_api.controller.backend;

import com.ms.crud_api.exception.BadRequestException;
import com.ms.crud_api.exception.NotFoundException;
import com.ms.crud_api.infrastructure.model.response.BaseResponse;
import com.ms.crud_api.infrastructure.model.response.body.BaseBodyResponse;
import com.ms.crud_api.infrastructure.model.response.error.ErrorResponse;
import com.ms.crud_api.model.entity.OrderEntity;
import com.ms.crud_api.model.entity.PaymentMethodEntity;
import com.ms.crud_api.model.entity.UserEntity;
import com.ms.crud_api.model.request.order.OrderRequest;
import com.ms.crud_api.model.request.order.OrderRestore;
import com.ms.crud_api.model.request.payment.PaymentRequest;
import com.ms.crud_api.model.request.user.UserRestoreRequest;
import com.ms.crud_api.model.response.Payment.PaymentResponse;
import com.ms.crud_api.model.response.order.OrderResponse;
import com.ms.crud_api.model.response.user.UserResponse;
import com.ms.crud_api.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Order", description = "Order Management")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @Operation(summary = "Endpoint for admin create a Order", description = "Admin can creating a Order by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "201", content = @Content(schema = @Schema(implementation = OrderResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @PostMapping
    public ResponseEntity<BaseBodyResponse> create(@Valid @RequestBody OrderRequest request) throws Exception {
        OrderEntity order = this.orderService.create(request);

        return BaseBodyResponse.createSuccess(OrderResponse.fromEntity(order), "Created Successfully");
    }

    @Operation(summary = "Endpoint for admin create a Order", description = "Admin can creating a Order by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "201", content = @Content(schema = @Schema(implementation = OrderResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    //update
    @PutMapping("/{id}")
    public ResponseEntity<BaseBodyResponse> update(@PathVariable Long id, @Valid @RequestBody OrderRequest request) throws Exception {
        OrderEntity orderEntity = this.orderService.update((id), request);

        return BaseBodyResponse.success(OrderResponse.fromEntity(orderEntity), "Updated Successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseBodyResponse> findOne(@PathVariable Long id) throws NotFoundException {
        OrderEntity orderEntity = this.orderService.findOne(id);

        return BaseBodyResponse.success(OrderResponse.fromEntity(orderEntity), "Found Successfully");
    }

    @Operation(summary = "Endpoint for admin find all Order", description = "Admin can find all Order by using this endpoint", responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = OrderResponse.class), mediaType = "application/json")),
                    @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
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
        Page<BaseResponse> orderEntity = this.orderService
                .findAll(page, limit, isPage, sort, isTrash, reqParam)
                .map(OrderResponse::fromEntity);

        // Return a successful response
        return BaseBodyResponse.success(orderEntity, "Fetched users successfully.");
    }

    @Operation(summary = "Endpoint for admin delete a Order", description = "Admin can delete a Order by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = OrderResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseBodyResponse> delete(@PathVariable Long id) throws Exception {
        OrderEntity orderEntity = this.orderService.delete(id);

        return BaseBodyResponse.success(OrderResponse.fromEntity(orderEntity), "Deleted Successfully");
    }

    @Operation(summary = "Endpoint for admin restore a Order", description = "Admin can restore a Order by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = OrderResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})

    @PutMapping("/restore/{id}")
    public ResponseEntity<BaseBodyResponse> restore(@PathVariable Long id, @RequestBody OrderRestore req) throws Exception {
        OrderEntity orderEntity = this.orderService.restore(id,req);

        return BaseBodyResponse.success(OrderResponse.fromEntity(orderEntity), "Restored Successfully");
    }

    @Operation(summary = "Endpoint for admin find a Order from trash", description = "Admin can find a Order from trash by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = OrderResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @GetMapping("/find-trash/{id}")
    public ResponseEntity<BaseBodyResponse>findOneWithSoftDeleted(@PathVariable Long id) throws NotFoundException{
        OrderEntity orderEntity = this.orderService.findOneWithSoftDeleted(id);
        return BaseBodyResponse.success(OrderResponse.fromEntity(orderEntity), "Found Successfully");
    }
    @Operation(summary = "Endpoint for admin delete a Order from trash", description = "Admin can delete a Order from trash by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = OrderResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @DeleteMapping("/delete-trash/{id}")
    public ResponseEntity<BaseBodyResponse>deleteSoftDeleted(@PathVariable Long id) throws Exception{
        OrderEntity orderEntity = this.orderService.deleteFromTrash(id);
        return BaseBodyResponse.success(OrderResponse.fromEntity(orderEntity), "Deleted Successfully");
    }

    @DeleteMapping("/force-delete/{id}")
    public ResponseEntity<BaseBodyResponse>forceDelete(@PathVariable Long id) throws Exception{
        OrderEntity orderEntity = this.orderService.forceDelete(id);
        return BaseBodyResponse.success(OrderResponse.fromEntity(orderEntity), "Deleted Successfully");
    }





}
