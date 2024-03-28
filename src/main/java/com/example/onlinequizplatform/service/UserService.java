package com.example.onlinequizplatform.service;

import com.example.onlinequizplatform.dto.UserCreateDto;
import com.example.onlinequizplatform.dto.UserDto;

public interface UserService {
    void registerUser(UserCreateDto userCreateDto);

    UserDto getUserByEmail(String email);
}
