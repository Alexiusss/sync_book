package com.example.sync_book.config;

import com.example.sync_book.error.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.example.sync_book.error.ErrorType.*;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    static final Map<Class<? extends Throwable>, ErrorType> HTTP_STATUS_MAP = new LinkedHashMap<>(){
        {
            put(NotFoundException.class, NOT_FOUND);
            put(FileNotFoundException.class, NOT_FOUND);
            put(NoHandlerFoundException.class, NOT_FOUND);
            put(DataConflictException.class, DATA_CONFLICT);
            put(IllegalRequestDataException.class, BAD_REQUEST);
            put(AppException.class, APP_ERROR);
            put(UnsupportedOperationException.class, APP_ERROR);
            put(IllegalArgumentException.class, BAD_DATA);
            put(ValidationException.class, BAD_REQUEST);
            put(HttpRequestMethodNotSupportedException.class, BAD_REQUEST);
            put(ServletRequestBindingException.class, BAD_REQUEST);
        }
    };

    @ExceptionHandler(Exception.class)
    ProblemDetail exception(Exception ex, HttpServletRequest request) {
        return ProblemDetail.forStatusAndDetail(HTTP_STATUS_MAP.get(ex).status, ex.getMessage());
    }
}