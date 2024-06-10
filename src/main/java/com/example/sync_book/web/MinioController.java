package com.example.sync_book.web;

import com.example.sync_book.service.MinioService;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for managing file upload and download using MinIO.
 */
@RestController
@RequestMapping(path = MinioController.REST_URL, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class MinioController {
    public static final String REST_URL = "/api/v1/minio";

    private final MinioService minioService;

    /**
     * Uploads a file to MinIO storage.
     *
     * @param file the file to upload.
     * @return the name of the uploaded file.
     */
    @Operation(summary = "Upload the file")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> upload(@Parameter(description = "File to upload") @RequestParam("file")
                                         @Schema(type = "string", format = "binary") MultipartFile file)
            throws ServerException, InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        minioService.save(file);
        return ResponseEntity.ok(file.getOriginalFilename());
    }

    /**
     * Downloads a file from MinIO storage.
     *
     * @param fileName the name of the file to download.
     * @return the byte content of the file.
     */
    @GetMapping
    @Operation(summary = "Download the file by its name")
    public ResponseEntity<byte[]> download(@RequestParam String fileName)
            throws ServerException, InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        byte[] content = minioService.get(fileName);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .body(content);
    }
}