package com.ms.crud_api.controller.backend;

import com.ms.crud_api.exception.BadRequestException;
import com.ms.crud_api.exception.NotFoundException;
import com.ms.crud_api.infrastructure.model.response.BaseResponse;
import com.ms.crud_api.infrastructure.model.response.body.BaseBodyResponse;
import com.ms.crud_api.infrastructure.model.response.error.ErrorResponse;
import com.ms.crud_api.model.entity.CategoryEntity;
import com.ms.crud_api.model.request.category.CategoryRequest;
import com.ms.crud_api.model.request.category.CategoryRestore;
import com.ms.crud_api.model.response.category.CategoryResponse;
import com.ms.crud_api.service.CategoryService;
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

@Tag(name = "Category", description = "Category Management")
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @Operation(summary = "Endpoint for admin create a category", description = "Admin can creating a category by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "201", content = @Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @PostMapping
    public ResponseEntity<BaseBodyResponse> create(@Valid @RequestBody CategoryRequest request) throws Exception {
        CategoryEntity category = this.categoryService.create(request);

        return BaseBodyResponse.createSuccess(CategoryResponse.fromEntity(category), "Created Successfully");
    }

    @Operation(summary = "Endpoint for admin update a category", description = "Admin can updating a category by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @PutMapping("/{id}")
    public ResponseEntity<BaseBodyResponse> update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) throws Exception {
        CategoryEntity category = this.categoryService.update((id), request);

        return BaseBodyResponse.success(CategoryResponse.fromEntity(category), "Updated Successfully");
    }

    @Operation(summary = "Endpoint for admin find all category", description = "Admin can find all category by using this endpoint", responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")),
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
        Page<BaseResponse> userEntity = this.categoryService
                .findAll(page, limit, isPage, sort, isTrash, reqParam)
                .map(CategoryResponse::fromEntity);

        // Return a successful response
        return BaseBodyResponse.success(userEntity, "Fetched users successfully.");
    }


    @Operation(summary = "Endpoint for admin find category",
            description = "Admin can find category by using this endpoint",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")),
                    @ApiResponse(description = "Error", responseCode = "400-500",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
            })
    @GetMapping("/{id}")
    public ResponseEntity<BaseBodyResponse> findOne(@PathVariable Long id) throws NotFoundException {
        CategoryEntity category = this.categoryService.findOne(id);

        return BaseBodyResponse.success(CategoryResponse.fromEntity(category), "Found Successfully");
    }

    @Operation(summary = "Endpoint for admin delete a category", description = "Admin can delete a category by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseBodyResponse> delete(@PathVariable Long id) throws Exception {
        CategoryEntity category = this.categoryService.delete(id);

        return BaseBodyResponse.success(CategoryResponse.fromEntity(category), "Deleted Successfully");
    }

    @Operation(summary = "Endpoint for admin restore a category", description = "Admin can restore a category by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})

    @PutMapping("/restore/{id}")
    public ResponseEntity<BaseBodyResponse> restore(@PathVariable Long id, @RequestBody CategoryRestore req) throws Exception {
        CategoryEntity category = this.categoryService.restore(id,req);

        return BaseBodyResponse.success(CategoryResponse.fromEntity(category), "Restored Successfully");
    }

    @Operation(summary = "Endpoint for admin find a category from trash", description = "Admin can find a category from trash by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @GetMapping("/find-trash/{id}")
    public ResponseEntity<BaseBodyResponse>findOneWithSoftDeleted(@PathVariable Long id) throws NotFoundException{
        CategoryEntity category = this.categoryService.findOneWithSoftDeleted(id);
        return BaseBodyResponse.success(CategoryResponse.fromEntity(category), "Found Successfully");
    }
    @Operation(summary = "Endpoint for admin delete a category from trash", description = "Admin can delete a category from trash by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @DeleteMapping("/delete-trash/{id}")
    public ResponseEntity<BaseBodyResponse>deleteSoftDeleted(@PathVariable Long id) throws Exception{
        CategoryEntity category = this.categoryService.deleteFromTrash(id);
        return BaseBodyResponse.success(CategoryResponse.fromEntity(category), "Deleted Successfully");
    }

    @DeleteMapping("/force-delete/{id}")
    public ResponseEntity<BaseBodyResponse>forceDelete(@PathVariable Long id) throws Exception{
        CategoryEntity category = this.categoryService.forceDelete(id);
        return BaseBodyResponse.success(CategoryResponse.fromEntity(category), "Deleted Successfully");
    }

}
