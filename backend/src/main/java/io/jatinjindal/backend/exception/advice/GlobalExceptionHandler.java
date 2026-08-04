package io.jatinjindal.backend.exception.advice;

import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleRequestValidationException(
            MethodArgumentNotValidException ex
    ) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage).toList();

        return new ResponseEntity<>(ErrorResponse.builder().timestamp(Instant.now())
                .error(ErrorType.INVALID_REQUEST_BODY.getMessage())
                .details(details).build(), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NonTransientAiException.class)
    public ResponseEntity<ErrorResponse> handleNonTransientAiException(
            NonTransientAiException ex
    ) {
        return new ResponseEntity<>(ErrorResponse.builder().timestamp(Instant.now())
                .error(ErrorType.INVALID_MODEL_DETAILS.getMessage())
                .details(List.of(ex.getMessage())).build(), HttpStatus.BAD_REQUEST
        );
    }
}
