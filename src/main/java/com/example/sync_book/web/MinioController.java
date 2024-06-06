package com.example.sync_book.web;

import com.example.sync_book.service.MinioService;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = MinioController.REST_URL, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class MinioController {
    public static final String REST_URL = "/api/v1/minio";

    @Autowired
    MinioClient minioClient;

    @Autowired
    MinioService minioService;

    @Operation(summary = "Upload the file")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> upload(@Parameter(description = "File to upload") @RequestParam("file")
                                    @Schema(type = "string", format = "binary") MultipartFile file) {
        minioService.save(file);
        return ResponseEntity.ok(file.getOriginalFilename());
    }

    @GetMapping
    @Operation(summary = "Download the file by its name")
    public ResponseEntity<?> download(@RequestParam String fileName) throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        byte[] file = minioService.get(fileName);
        return ResponseEntity.ok(file);
    }
}