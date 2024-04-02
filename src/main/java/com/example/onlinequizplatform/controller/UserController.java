package com.example.onlinequizplatform.controller;

import com.example.onlinequizplatform.dto.UserCreateDto;
import com.example.onlinequizplatform.dto.UserStatisticsDto;
import com.example.onlinequizplatform.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        userService.registerUser(userCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @GetMapping("/users/{userId}/statistics")
    public ResponseEntity<UserStatisticsDto> getUserStatistics(@PathVariable Long userId) {
        UserStatisticsDto userStatisticsDto = userService.getUserStatistics(userId);
        return ResponseEntity.ok(userStatisticsDto);
    }
}
