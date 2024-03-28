package com.example.onlinequizplatform.service;

import com.example.onlinequizplatform.models.Option;

public interface OptionService {
    Long createOption(Option option);
    void updateOption(Option option);
}