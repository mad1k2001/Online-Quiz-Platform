package com.example.onlinequizplatform.exeptions;

import java.util.NoSuchElementException;

public class CustomException extends NoSuchElementException {
    public CustomException(String message) {
        super(message);
    }
}
