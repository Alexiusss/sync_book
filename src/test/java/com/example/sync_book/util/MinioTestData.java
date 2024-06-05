package com.example.sync_book.util;

import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

public class MinioTestData {
        public static MockMultipartFile getNewFile(String fileName, String mediaType) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return new MockMultipartFile("file", fileName, mediaType, loader.getResourceAsStream(fileName));
    }
}