package com.example.wallet.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Error> catchResourceNotFoundException(NotFoundWalletException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new Error(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Error> catchResourceNotFoundException(LowFoundsException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new Error(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Error> catchResourceNotFoundException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        log.error(ex.getMessage(), request);
        String error = ex.getName() + " должен быть типа " + Objects.requireNonNull(ex.getRequiredType()).getName();
        return new ResponseEntity<>(new Error(HttpStatus.BAD_REQUEST.value(), error), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Error> catchResourceNotFoundException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> list = new ArrayList<>();
        fieldErrors.forEach(fieldError ->
                list.add(fieldError.getField())
        );
        String errorStr = "Null values: " + String.join(",", list);
        return new ResponseEntity<>(new Error(HttpStatus.BAD_REQUEST.value(), errorStr), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Error> catchResourceNotFoundException(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage());
        String error = ex.getMessage();
        return new ResponseEntity<>(new Error(HttpStatus.BAD_REQUEST.value(), error), HttpStatus.BAD_REQUEST);
    }

}