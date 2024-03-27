package com.example.onlinequizplatform.service.impl;

import com.example.onlinequizplatform.dao.UserDao;
import com.example.onlinequizplatform.dto.UserCreateDto;
import com.example.onlinequizplatform.models.User;
import com.example.onlinequizplatform.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(UserCreateDto userCreateDto) {
        User user = User.builder()
                .name(userCreateDto.getName())
                .email(userCreateDto.getEmail())
                .password(passwordEncoder.encode(userCreateDto.getPassword()))
                .build();

        userDao.save(user);
    }
}
