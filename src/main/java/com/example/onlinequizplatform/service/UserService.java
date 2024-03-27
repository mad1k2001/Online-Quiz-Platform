package com.example.onlinequizplatform.service;

import com.example.onlinequizplatform.dto.UserCreateDto;

public interface UserService {
    void registerUser(UserCreateDto userCreateDto);
}
