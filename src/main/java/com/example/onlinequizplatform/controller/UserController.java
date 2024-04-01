package com.example.onlinequizplatform.controller;

import com.example.onlinequizplatform.dto.UserCreateDto;
import com.example.onlinequizplatform.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserCreateDto userCreateDto) {
        userService.registerUser(userCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

}
