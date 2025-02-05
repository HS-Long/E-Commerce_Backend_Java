package com.ms.crud_api.controller.backend;


import com.ms.crud_api.exception.BadRequestException;
import com.ms.crud_api.exception.NotFoundException;
import com.ms.crud_api.infrastructure.model.response.BaseResponse;
import com.ms.crud_api.infrastructure.model.response.body.BaseBodyResponse;
import com.ms.crud_api.infrastructure.model.response.error.ErrorResponse;
import com.ms.crud_api.model.entity.UserEntity;
import com.ms.crud_api.model.request.user.UserRequest;
import com.ms.crud_api.model.request.user.UserRestoreRequest;
import com.ms.crud_api.model.response.user.UserResponse;
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

@Tag(name = "User", description = "User Management")
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }




    @Operation(summary = "Endpoint for admin create a account", description = "Admin can creating a account by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "201", content = @Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @PostMapping("/create")
    public ResponseEntity<BaseBodyResponse> create(@Valid @RequestBody UserRequest request) throws Exception {
        UserEntity userEntity = this.userService.create(request);

        return BaseBodyResponse.createSuccess(UserResponse.fromEntity(userEntity), "Created Successfully");
    }

    @Operation(summary = "Endpoint for admin update a account", description = "Admin can updating a account by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @PutMapping("/update/{id}")
    public ResponseEntity<BaseBodyResponse> update(@PathVariable Long id, @Valid @RequestBody UserRequest request) throws Exception {
        UserEntity userEntity = this.userService.update((id), request);

        return BaseBodyResponse.success(UserResponse.fromEntity(userEntity), "Updated Successfully");
    }

    @Operation(summary = "Endpoint for admin find all Users",
            description = "Admin can find all Users by using this endpoint",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")),
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
        Page<BaseResponse> userEntity = this.userService
                .findAll(page, limit, isPage, sort, isTrash, reqParam)
                .map(UserResponse::fromEntity);

        // Return a successful response
        return BaseBodyResponse.success(userEntity, "Fetched users successfully.");
    }


    @Operation(summary = "Endpoint for admin find Users",
            description = "Admin can find Users by using this endpoint",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")),
                    @ApiResponse(description = "Error", responseCode = "400-500",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
            })
    @GetMapping("/search/{id}")
    public ResponseEntity<BaseBodyResponse> findOne(@PathVariable Long id) throws NotFoundException {
        UserEntity userEntity = this.userService.findOne(id);

        return BaseBodyResponse.success(UserResponse.fromEntity(userEntity), "Found Successfully");
    }

    @Operation(summary = "Endpoint for admin delete a account", description = "Admin can delete a account by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseBodyResponse> delete(@PathVariable Long id) throws Exception {
        UserEntity userEntity = this.userService.delete(id);

        return BaseBodyResponse.success(UserResponse.fromEntity(userEntity), "Deleted Successfully");
    }

    @Operation(summary = "Endpoint for admin restore a user", description = "Admin can restore a user by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})

    @PutMapping("/restore/{id}")
    public ResponseEntity<BaseBodyResponse> restore(@PathVariable Long id, @RequestBody UserRestoreRequest req) throws Exception {
        UserEntity userEntity = this.userService.restore(id,req);

        return BaseBodyResponse.success(UserResponse.fromEntity(userEntity), "Restored Successfully");
    }

    @Operation(summary = "Endpoint for admin find a user from trash", description = "Admin can find a user from trash by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @GetMapping("/find-trash/{id}")
    public ResponseEntity<BaseBodyResponse>findOneWithSoftDeleted(@PathVariable Long id) throws NotFoundException{
        UserEntity userEntity = this.userService.findOneWithSoftDeleted(id);
        return BaseBodyResponse.success(UserResponse.fromEntity(userEntity), "Found Successfully");
    }
    @Operation(summary = "Endpoint for admin delete a user from trash", description = "Admin can delete a user from trash by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))})
    @DeleteMapping("/delete-trash/{id}")
    public ResponseEntity<BaseBodyResponse>deleteSoftDeleted(@PathVariable Long id) throws Exception{
        UserEntity userEntity = this.userService.deleteFromTrash(id);
        return BaseBodyResponse.success(UserResponse.fromEntity(userEntity), "Deleted Successfully");
    }

    @DeleteMapping("/force-delete/{id}")
    public ResponseEntity<BaseBodyResponse>forceDelete(@PathVariable Long id) throws Exception{
        UserEntity userEntity = this.userService.ForceDelete(id);
        return BaseBodyResponse.success(UserResponse.fromEntity(userEntity), "Deleted Successfully");
    }




}
