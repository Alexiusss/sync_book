package com.example.sync_book.error;

public class NotFoundException extends AppException{
    public NotFoundException(String msg) {
        super(msg);
    }
}