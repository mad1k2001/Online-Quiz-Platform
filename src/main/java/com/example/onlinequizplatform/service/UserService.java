package com.example.onlinequizplatform.service;

import com.example.onlinequizplatform.dto.UserCreateDto;
import com.example.onlinequizplatform.dto.UserDto;
import com.example.onlinequizplatform.dto.UserStatisticsDto;

public interface UserService {
    void registerUser(UserCreateDto userCreateDto);
    UserDto getUserByEmail(String email);
    UserStatisticsDto getUserStatistics(Long userId);
}
