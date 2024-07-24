package com.example.sync_book.config;

import com.example.sync_book.error.*;
import io.minio.errors.MinioException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.NonNull;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.sync_book.error.ErrorType.*;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    public static final String ERR_PFX = "ERR# ";

    @Getter
    private final MessageSource messageSource;
    
    static final Map<Class<? extends Throwable>, ErrorType> HTTP_STATUS_MAP = new LinkedHashMap<>() {
        {
            put(NotFoundException.class, NOT_FOUND);
            put(FileNotFoundException.class, NOT_FOUND);
            put(NoHandlerFoundException.class, NOT_FOUND);
            put(MinioException.class, APP_ERROR);
            put(DataConflictException.class, DATA_CONFLICT);
            put(IllegalRequestDataException.class, BAD_REQUEST);
            put(AppException.class, APP_ERROR);
            put(UnsupportedOperationException.class, APP_ERROR);
            put(IllegalArgumentException.class, BAD_DATA);
            put(ValidationException.class, BAD_REQUEST);
            put(HttpRequestMethodNotSupportedException.class, BAD_REQUEST);
            put(ServletRequestBindingException.class, BAD_REQUEST);
            put(MethodArgumentNotValidException.class, BAD_DATA);
        }
    };

    @ExceptionHandler(Exception.class)
    private ProblemDetail exception(Exception ex, HttpServletRequest request) {
        return processException(ex, request, Map.of());
    }

    private ProblemDetail processException(@NonNull Exception ex, HttpServletRequest request, Map<String, Object> additionalParams) {
        String path = request.getRequestURI();
        Class<? extends Exception> exClass = ex.getClass();
        Optional<ErrorType> optType = HTTP_STATUS_MAP.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(exClass))
                .findAny()
                .map(Map.Entry::getValue);
        if(optType.isPresent()) {
            log.error(ERR_PFX + "Exception {} at request {}", ex, path);
            return createProblemDetail(ex, path, optType.get(), ex.getMessage(), additionalParams);
        } else {
            Throwable root = getRootCause(ex);
            log.error(ERR_PFX + "Exception " + root + " at request " + path, root);
            return createProblemDetail(ex, path, APP_ERROR, "Exception " + root.getClass().getSimpleName(), additionalParams);
        }
    }

    private ProblemDetail createProblemDetail(Exception ex, String path, ErrorType type, String defaultDetail, Map<String, Object> additionalParams) {
        ErrorResponse.Builder builder = ErrorResponse.builder(ex, type.status, defaultDetail);
        ProblemDetail pd = builder
                .title(type.title).instance(URI.create(path))
                .build().updateAndGetBody(messageSource, LocaleContextHolder.getLocale());
        additionalParams.forEach(pd::setProperty);
        return pd;
    }

    @NonNull
    private static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }
}