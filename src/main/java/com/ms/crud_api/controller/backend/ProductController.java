//package com.ms.crud_api.controller.backend;
//
//import com.ms.crud_api.exception.BadRequestException;
//import com.ms.crud_api.exception.NotFoundException;
//import com.ms.crud_api.infrastructure.model.response.BaseResponse;
//import com.ms.crud_api.infrastructure.model.response.body.BaseBodyResponse;
//import com.ms.crud_api.infrastructure.model.response.error.ErrorResponse;
//import com.ms.crud_api.model.entity.ProductEntity;
//import com.ms.crud_api.model.entity.UserEntity;
//import com.ms.crud_api.model.request.product.ProductRequest;
//import com.ms.crud_api.model.request.product.ProductRestore;
//import com.ms.crud_api.model.request.user.UserRequest;
//import com.ms.crud_api.model.request.user.UserRestoreRequest;
//import com.ms.crud_api.model.response.product.ProductResponse;
//import com.ms.crud_api.model.response.user.UserResponse;
//import com.ms.crud_api.service.ProductService;
//import com.ms.crud_api.service.UserService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//
//@Tag(name = "Product", description = "User Management")
//@RestController
//@RequestMapping("/api/product")
//public class ProductController {
//
//
//    private final ProductService productService;
//
//    @Autowired
//    public ProductController(ProductService productService) {
//        this.productService = productService;
//    }
//
//
//    @Operation(summary = "Endpoint for admin create a Product", description = "Admin can creating a Product by using this endpoint", responses = {
//            @ApiResponse(description = "Success", responseCode = "201", content = @Content(schema = @Schema(implementation = ProductResponse.class), mediaType = "application/json")),
//            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
//    @PostMapping
//    public ResponseEntity<BaseBodyResponse> create(@Valid @RequestBody ProductRequest request) throws Exception {
//        ProductEntity product = this.productService.create(request);
//
//        return BaseBodyResponse.createSuccess(ProductResponse.fromEntity(product), "Created Successfully");
//    }
//
//    @Operation(summary = "Endpoint for admin update a Product", description = "Admin can updating a Product by using this endpoint", responses = {
//            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = ProductResponse.class), mediaType = "application/json")),
//            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
//    @PutMapping("/{id}")
//    public ResponseEntity<BaseBodyResponse> update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) throws Exception {
//        ProductEntity product = this.productService.update((id), request);
//
//        return BaseBodyResponse.success(ProductResponse.fromEntity(product), "Updated Successfully");
//    }
//
//    @Operation(summary = "Endpoint for admin find all Product",
//            description = "Admin can find all Product by using this endpoint",
//            responses = {
//                    @ApiResponse(description = "Success", responseCode = "200",
//                            content = @Content(schema = @Schema(implementation = ProductResponse.class), mediaType = "application/json")),
//                    @ApiResponse(description = "Error", responseCode = "400-500",
//                            content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
//            })
//    @GetMapping
//    public ResponseEntity<BaseBodyResponse> findAll(
//            @RequestParam(name = "page", defaultValue = "1") int page,
//            @RequestParam(name = "limit", defaultValue = "10") int limit,
//            @RequestParam(name = "isPage", required = false, defaultValue = "true") boolean isPage,
//            @RequestParam(name = "sort", required = false, defaultValue = "id:desc") String sort,
//            @RequestParam(name = "isTrash", required = false, defaultValue = "false") boolean isTrash,
//            @RequestParam Map<String, String> reqParam) throws BadRequestException {
//
//        // Validate page and limit parameters
//        if (page < 1 || limit < 1) {
//            throw new BadRequestException("Page and limit must be greater than 0.");
//        }
//
//        // Fetch flights and map to FlightResponse
//        Page<BaseResponse> product = this.productService
//                .findAll(page, limit, isPage, sort, isTrash, reqParam)
//                .map(ProductResponse::fromEntity);
//
//        // Return a successful response
//        return BaseBodyResponse.success(product, "Fetched users successfully.");
//    }
//
//
//    @Operation(summary = "Endpoint for admin find Product",
//            description = "Admin can find Product by using this endpoint",
//            responses = {
//                    @ApiResponse(description = "Success", responseCode = "200",
//                            content = @Content(schema = @Schema(implementation = ProductResponse.class), mediaType = "application/json")),
//                    @ApiResponse(description = "Error", responseCode = "400-500",
//                            content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
//            })
//    @GetMapping("/{id}")
//    public ResponseEntity<BaseBodyResponse> findOne(@PathVariable Long id) throws NotFoundException {
//        ProductEntity product = this.productService.findOne(id);
//
//        return BaseBodyResponse.success(ProductResponse.fromEntity(product), "Found Successfully");
//    }
//
//    @Operation(summary = "Endpoint for admin delete a Product", description = "Admin can delete a Product by using this endpoint", responses = {
//            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = ProductResponse.class), mediaType = "application/json")),
//            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
//    @DeleteMapping("/{id}")
//    public ResponseEntity<BaseBodyResponse> delete(@PathVariable Long id) throws Exception {
//        ProductEntity product = this.productService.delete(id);
//
//        return BaseBodyResponse.success(ProductResponse.fromEntity(product), "Deleted Successfully");
//    }
//
//    @Operation(summary = "Endpoint for admin restore a Product", description = "Admin can restore a Product by using this endpoint", responses = {
//            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = ProductResponse.class), mediaType = "application/json")),
//            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
//
//    @PutMapping("/restore/{id}")
//    public ResponseEntity<BaseBodyResponse> restore(@PathVariable Long id, @RequestBody ProductRestore req) throws Exception {
//        ProductEntity product = this.productService.restore(id,req);
//
//        return BaseBodyResponse.success(ProductResponse.fromEntity(product), "Restored Successfully");
//    }
//
//    @Operation(summary = "Endpoint for admin find a Product from trash", description = "Admin can find a Product from trash by using this endpoint", responses = {
//            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = ProductResponse.class), mediaType = "application/json")),
//            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
//    @GetMapping("/find-trash/{id}")
//    public ResponseEntity<BaseBodyResponse>findOneWithSoftDeleted(@PathVariable Long id) throws NotFoundException{
//        ProductEntity product = this.productService.findOneWithSoftDeleted(id);
//        return BaseBodyResponse.success(ProductResponse.fromEntity(product), "Found Successfully");
//    }
//    @Operation(summary = "Endpoint for admin delete a Product from trash", description = "Admin can delete a Product from trash by using this endpoint", responses = {
//            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = ProductResponse.class), mediaType = "application/json")),
//            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
//    @DeleteMapping("/delete-trash/{id}")
//    public ResponseEntity<BaseBodyResponse>deleteSoftDeleted(@PathVariable Long id) throws Exception{
//        ProductEntity product = this.productService.deleteFromTrash(id);
//        return BaseBodyResponse.success(ProductResponse.fromEntity(product), "Deleted Successfully");
//    }
//
//    @DeleteMapping("/force-delete/{id}")
//    public ResponseEntity<BaseBodyResponse>forceDelete(@PathVariable Long id) throws Exception{
//        ProductEntity product = this.productService.ForceDelete(id);
//        return BaseBodyResponse.success(ProductResponse.fromEntity(product), "Deleted Successfully");
//    }
//
//
//
//}
