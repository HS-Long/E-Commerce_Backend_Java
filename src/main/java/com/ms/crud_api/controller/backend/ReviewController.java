package com.ms.crud_api.controller.backend;


import com.ms.crud_api.exception.BadRequestException;
import com.ms.crud_api.exception.NotFoundException;
import com.ms.crud_api.infrastructure.model.response.BaseResponse;
import com.ms.crud_api.infrastructure.model.response.body.BaseBodyResponse;
import com.ms.crud_api.infrastructure.model.response.error.ErrorResponse;
import com.ms.crud_api.model.entity.ProductEntity;
import com.ms.crud_api.model.entity.ReviewEntity;
import com.ms.crud_api.model.request.product.ProductRequest;
import com.ms.crud_api.model.request.product.ProductRestore;
import com.ms.crud_api.model.request.review.ReviewRequest;
import com.ms.crud_api.model.response.product.ProductResponse;
import com.ms.crud_api.model.response.review.ReviewResponse;
import com.ms.crud_api.service.ReviewService;
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

@Tag(name = "Review", description = "Review Management")
@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "Endpoint for admin create a Review", description = "Admin can creating a Review by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "201", content = @Content(schema = @Schema(implementation = ReviewResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @PostMapping
    public ResponseEntity<BaseBodyResponse> create(@Valid @RequestBody ReviewRequest request) throws Exception {
        ReviewEntity review = this.reviewService.create(request);

        return BaseBodyResponse.createSuccess(ReviewResponse.fromEntity(review), "Created Successfully");
    }



    @Operation(summary = "Endpoint for admin update a Review", description = "Admin can updating a Review by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = ReviewResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @PutMapping("/{id}")
    public ResponseEntity<BaseBodyResponse> update(@PathVariable Long id, @Valid @RequestBody ReviewRequest request) throws Exception {
        ReviewEntity review = this.reviewService.update((id), request);

        return BaseBodyResponse.success(ReviewResponse.fromEntity(review), "Updated Successfully");
    }

    @Operation(summary = "Endpoint for admin find all Review",
            description = "Admin can find all Review by using this endpoint",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ReviewResponse.class), mediaType = "application/json")),
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
        Page<BaseResponse> product = this.reviewService
                .findAll(page, limit, isPage, sort, isTrash, reqParam)
                .map(ReviewResponse::fromEntity);

        // Return a successful response
        return BaseBodyResponse.success(product, "Fetched users successfully.");
    }


    @Operation(summary = "Endpoint for admin find Review",
            description = "Admin can find Review by using this endpoint",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ReviewResponse.class), mediaType = "application/json")),
                    @ApiResponse(description = "Error", responseCode = "400-500",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
            })
    @GetMapping("/{id}")
    public ResponseEntity<BaseBodyResponse> findOne(@PathVariable Long id) throws NotFoundException {
        ReviewEntity review = this.reviewService.findOne(id);

        return BaseBodyResponse.success(ReviewResponse.fromEntity(review), "Found Successfully");
    }




    @Operation(summary = "Endpoint for admin force delete a Review", description = "Admin can force delete a Review by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = ReviewResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})

    @DeleteMapping("/force-delete/{id}")
    public ResponseEntity<BaseBodyResponse>forceDelete(@PathVariable Long id) throws Exception{
        ReviewEntity review = this.reviewService.ForceDelete(id);
        return BaseBodyResponse.success(ReviewResponse.fromEntity(review), "Deleted Successfully");
    }



}
