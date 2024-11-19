package com.ctf.notekeeper.Misc;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomResponse> handleAccessDeniedException(AccessDeniedException e) {
        return createErrorResponse(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<CustomResponse> handleNoSuchElementException(NoSuchElementException e) {
        return createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<CustomResponse> createErrorResponse(String message, HttpStatus status) {
        CustomResponse response = CustomResponseFactory.createResponse("error", message);
        return new ResponseEntity<>(response, status);
    }
}
