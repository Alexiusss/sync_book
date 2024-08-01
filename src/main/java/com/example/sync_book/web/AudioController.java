package com.example.sync_book.web;

import com.example.sync_book.service.MinioService;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.example.sync_book.util.AudioUtil.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = AudioController.REST_URL, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AudioController {

    public static final String REST_URL = "/api/v1/sync";

    private final MinioService minioService;

    @GetMapping("/{fileName}")
    @Operation(summary = "Download part of the file by its name")
    public ResponseEntity<byte[]> readChunk(
            @PathVariable String fileName,
            @RequestHeader(value = HttpHeaders.RANGE, defaultValue = "bytes=0-") String range
    )
            throws ServerException, InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException
    {
        long fileSize = minioService.getFileSize(fileName);
        long startRange = parseStartRange(range);
        long endRange = parseEndRange(range, fileSize);
        byte[] content = minioService.get(fileName, startRange, endRange);

        return ResponseEntity
                .status(HttpStatus.PARTIAL_CONTENT)
                .header(CONTENT_TYPE, AUDIO_MPEG)
                .header(ACCEPT_RANGES, ACCEPTS_RANGES_VALUE)
                .header(CONTENT_LENGTH, calculateContentLength(startRange, endRange))
                .header(CONTENT_RANGE, getContentRangeValue(startRange, endRange, fileSize))
                .body(content);
    }
}