package com.ms.crud_api.model.response.file;

import com.ms.crud_api.infrastructure.model.response.BaseResponse;

import java.util.List;

public class FileListResponse extends BaseResponse {


    private List<FileResponse> fileResponses;

    public FileListResponse(List<FileResponse> fileResponses) {
        this.fileResponses = fileResponses;
    }

    public List<FileResponse> getFileResponses() {
        return fileResponses;
    }

    public void setFileResponses(List<FileResponse> fileResponses) {
        this.fileResponses = fileResponses;
    }
}
