package com.example.onlinequizplatform.service.impl;

import com.example.onlinequizplatform.dao.UserDao;
import com.example.onlinequizplatform.dto.UserCreateDto;
import com.example.onlinequizplatform.dto.UserDto;
import com.example.onlinequizplatform.exeptions.CustomException;
import com.example.onlinequizplatform.models.User;
import com.example.onlinequizplatform.service.AuthorityService;
import com.example.onlinequizplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityService authorityService;


    @Override
    public void registerUser(UserCreateDto userCreateDto) {
        User user = User.builder()
                .name(userCreateDto.getName())
                .email(userCreateDto.getEmail())
                .password(passwordEncoder.encode(userCreateDto.getPassword()))
                .roleId(authorityService.getRoles("ADMIN").getRole())
                .build();

        userDao.save(user);
    }

    @Override
    public UserDto getUserByEmail(String email){
        Optional<User> user = userDao.getUsersByEmail(email);
        if(user.isEmpty()){
            throw new CustomException("User is not found");
        }
        return UserDto.builder()
                .id(user.get().getId())
                .name(user.get().getName())
                .email(user.get().getEmail())
                .build();
    }

}
