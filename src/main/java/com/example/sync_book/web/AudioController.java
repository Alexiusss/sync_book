package com.example.sync_book.web;

import com.example.sync_book.service.MinioService;
import com.example.sync_book.service.SyncService;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = AudioController.REST_URL, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AudioController {

    public static final String REST_URL = "/api/v1/sync";
    private static final String AUDIO_MPEG = "audio/mpeg";

    private final MinioService minioService;
    private final SyncService syncService;

    @GetMapping("/{fileName}")
    @Operation(summary = "Download part of the file by its name")
    public ResponseEntity<byte[]> readChunk(
            @PathVariable String fileName,
            @RequestParam(required = false, defaultValue = "0.00f") float lastPosition
    )
            throws ServerException, InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException
    {
        long fileSize = minioService.getFileSize(fileName);
        long startRange = syncService.calculateStartRange(fileSize, lastPosition);
        long endRange = syncService.calculateEndRange(fileSize, startRange);

        byte[] content = minioService.get(fileName, startRange, endRange);
        return ResponseEntity
                .status(HttpStatus.PARTIAL_CONTENT)
                .header(CONTENT_TYPE, AUDIO_MPEG)
                .header(ACCEPT_RANGES, "bytes")
                .header(CONTENT_LENGTH, String.valueOf(endRange - startRange))
                .header(CONTENT_RANGE, "bytes " + startRange + "-" + endRange + "/" + fileSize)
                .body(content);
    }
}