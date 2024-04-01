package com.example.onlinequizplatform.exeptions;

import com.example.onlinequizplatform.service.ErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorService error;

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseBody> noSuchElement(NoSuchElementException e) {
        return new ResponseEntity<>(error.makeResponseEx(e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity validHandler(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(error.makeResponseBind(ex), HttpStatus.BAD_REQUEST);
    }
}
