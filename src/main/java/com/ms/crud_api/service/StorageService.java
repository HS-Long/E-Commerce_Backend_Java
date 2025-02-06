package com.ms.crud_api.service;


import com.ms.crud_api.exception.InternalServerErrorException;
import com.ms.crud_api.exception.NotFoundException;
import com.ms.crud_api.util.FileUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {

    private static final String FILE_PATH = System.getProperty("user.dir") + "/files";

    public String upload(MultipartFile file) throws Exception{
        return FileUtil.saveMultipartFile(file, FILE_PATH);
    }

    public void deleteFile(String fileName) throws Exception{
        Path p = Paths.get(FILE_PATH);
        Path f = p.resolve(fileName);

        try {
            if (Files.exists(f)) {
                Files.delete(f);
            }
        } catch (Exception ex) {
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    public void loadFile(String filename, HttpServletResponse response) throws Exception{
        try {
            Path p = Paths.get(FILE_PATH).resolve(filename);
            Resource resource = new UrlResource(p.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new NotFoundException("File not found!");
            }

            response.setHeader(HttpHeaders.CONTENT_TYPE, Files.probeContentType(p));
            response.setHeader(HttpHeaders.CONTENT_LENGTH, "" + Files.size(p));
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + "\"" + filename + "\"");

            FileCopyUtils.copy(resource.getInputStream(), response.getOutputStream());

        } catch (Exception ex) {
            if (ex instanceof NotFoundException) throw new NotFoundException(ex.getMessage());

            throw new InternalServerErrorException(ex.getMessage());
        }
    }

}
