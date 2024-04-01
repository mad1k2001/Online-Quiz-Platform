package com.example.onlinequizplatform.service;

import com.example.onlinequizplatform.exeptions.ErrorResponseBody;
import org.springframework.validation.BindingResult;

public interface ErrorService {

    ErrorResponseBody makeResponseEx(Exception exception);

    ErrorResponseBody makeResponseBind(BindingResult ex);
}
