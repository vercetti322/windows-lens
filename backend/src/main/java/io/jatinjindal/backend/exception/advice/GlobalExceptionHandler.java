package io.jatinjindal.backend.exception.advice;

import io.jatinjindal.backend.exception.WindowsLensException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WindowsLensException.class)
    public ResponseEntity<String> handleWindowsLensException(
            WindowsLensException e
    ) {
        return ResponseEntity.status(HttpStatus
                .INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
