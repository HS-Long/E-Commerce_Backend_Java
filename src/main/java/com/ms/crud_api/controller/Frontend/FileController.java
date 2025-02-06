package com.ms.crud_api.controller.Frontend;

import com.ms.crud_api.constant.enums.RestURIConstant;
import com.ms.crud_api.exception.BadRequestException;
import com.ms.crud_api.infrastructure.model.response.BaseResponse;
import com.ms.crud_api.infrastructure.model.response.body.BaseBodyResponse;
import com.ms.crud_api.infrastructure.model.response.error.ErrorResponse;
import com.ms.crud_api.model.entity.FileEntity;
import com.ms.crud_api.model.request.file.UpdateFileNameRequest;
import com.ms.crud_api.model.response.file.FileListResponse;
import com.ms.crud_api.model.response.file.FileResponse;
import com.ms.crud_api.property.AppProperties;
import com.ms.crud_api.service.FileService;
import com.ms.crud_api.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Backend File Controller", description = "Controller for admin manage File")
@RestController
@RequestMapping(value = RestURIConstant.FILE)
public class FileController {

    private final StorageService storageService;
    private final FileService fileService;
    private final AppProperties appProperties;

    @Autowired
    public FileController(FileService fileService, AppProperties appProperties , StorageService storageService) {
        this.fileService = fileService;
        this.appProperties = appProperties;
        this.storageService = storageService;
    }


    private BaseResponse convertToBaseResponse(List<FileResponse> fileResponses) {
        // Create a new BaseResponse object and set the necessary fields
        // For simplicity, you can create a wrapper class that extends BaseResponse
        // and holds the list of FileResponse objects
        return new FileListResponse(fileResponses);
    }

    @Operation(summary = "Endpoint for admin upload a File", description = "Admin can upload a file by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "201", content = @Content(schema = @Schema(implementation = FileResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
    })
    @PostMapping(value = "/upload" , consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseBodyResponse> upload(@RequestPart MultipartFile file) throws Exception {
        FileEntity fileEntity = this.fileService.upload(file);
        return BaseBodyResponse.createSuccess(FileResponse.toResponse(fileEntity, appProperties), "File uploaded successfully.");
    }

    @Operation(summary = "Endpoint for admin batch upload Files", description = "Admin can batch upload files by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "201", content = @Content(array = @ArraySchema(schema = @Schema(implementation = FileResponse.class)), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
    })
    @PostMapping(value = "/batch-upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseBodyResponse> batchUpload(@RequestPart List<MultipartFile> files) throws Exception {
        List<FileEntity> fileEntities = this.fileService.batchUpload(files);
        List<FileResponse> fileResponses = FileResponse.toResponse(fileEntities, appProperties);
        BaseResponse baseResponse = convertToBaseResponse(fileResponses);
        return BaseBodyResponse.createSuccess(baseResponse, "Files uploaded successfully.");
    }

    @Operation(summary = "Endpoint for admin find all Files", description = "Admin can find all files by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = FileResponse.class)), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<BaseBodyResponse> findAll(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "isTrash", required = false, defaultValue = "false") boolean isTrash,
            @RequestParam(name = "q", required = false, defaultValue = "") String q) throws BadRequestException {

        if (page < 1 || limit < 1) {
            throw new BadRequestException("Page and limit must be greater than 0.");
        }

        Page<FileEntity> files = this.fileService.findAll(page, limit, isTrash, q);
        return BaseBodyResponse.success(files.map(file -> FileResponse.toResponse(file, appProperties)), "Fetched files successfully.");

    }

//    @Operation(summary = "Endpoint for admin find a File", description = "Admin can find a file by using this endpoint", responses = {
//            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = FileResponse.class), mediaType = "application/json")),
//            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
//    })
//    @GetMapping("/{id}")
//    public ResponseEntity<BaseBodyResponse> findOne(@PathVariable Long id) throws NotFoundException {
//        FileEntity file = this.fileService.findOne(id);
//        return BaseBodyResponse.success(FileResponse.toResponse(file, appProperties), "Found Successfully");
//    }

    @Operation(summary = "Endpoint for admin update a File name", description = "Admin can update a file name by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = FileResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<BaseBodyResponse> updateFileName(@PathVariable Long id, @Valid @RequestBody UpdateFileNameRequest request) throws Exception {
        FileEntity file = this.fileService.updateFileName(id, request);
        return BaseBodyResponse.success(FileResponse.toResponse(file, appProperties), "Updated Successfully");
    }

    @Operation(summary = "Endpoint for admin restore a File", description = "Admin can restore a file by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = FileResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
    })
    @PutMapping("/restore/{id}")
    public ResponseEntity<BaseBodyResponse> restore(@PathVariable Long id) throws Exception {
        FileEntity file = this.fileService.restore(id);
        return BaseBodyResponse.success(FileResponse.toResponse(file, appProperties), "Restored Successfully");
    }

    @Operation(summary = "Endpoint for admin delete a File", description = "Admin can delete a file by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = FileResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseBodyResponse> delete(@PathVariable Long id) throws Exception {
        FileEntity file = this.fileService.delete(id);
        return BaseBodyResponse.success((BaseResponse) FileResponse.toResponse(file, appProperties), "Deleted Successfully");
    }

    @Operation(summary = "Endpoint for admin force delete a File", description = "Admin can force delete a file by using this endpoint", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = FileResponse.class), mediaType = "application/json")),
            @ApiResponse(description = "Error", responseCode = "400-500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
    })
    @DeleteMapping("/force-delete/{id}")
    public ResponseEntity<BaseBodyResponse> forceDelete(@PathVariable Long id) throws Exception {
        this.fileService.forceDelete(id);
        return BaseBodyResponse.success((BaseResponse) null, "Force Deleted Successfully");
    }


    @GetMapping("/load/{filename}")
    public void loadFile(@PathVariable String filename, HttpServletResponse response) throws Exception {
        this.storageService.loadFile(filename, response);
    }
}

