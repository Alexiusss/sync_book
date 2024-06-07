package com.example.sync_book.error;

import org.springframework.lang.NonNull;

public class AppException extends RuntimeException {
    public AppException(@NonNull String message) {
        super(message);
    }
}