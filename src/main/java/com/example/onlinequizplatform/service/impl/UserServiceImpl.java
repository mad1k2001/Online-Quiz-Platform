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

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityService authorityService;


    @Override
    public void registerUser(UserCreateDto userCreateDto) {
        Optional<User> userCheck = userDao.getUsersByEmail(userCreateDto.getEmail());
        if(!userCheck.isEmpty()){
            String error="There is already a user with this email";
            log.error(error);
            throw new CustomException(error);
        }
        User user = new User();
        user.setName(userCreateDto.getName());
        user.setEmail(userCreateDto.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setRoleId(authorityService.getRoles("ADMIN").getId());

        userDao.save(user);
    }

    @Override
    public UserDto getUserByEmail(String email){
        Optional<User> user = userDao.getUsersByEmail(email);
        if(user.isEmpty()){
            String error="User is not found";
            log.error(error);
            throw new CustomException(error);
        }
        return UserDto.builder()
                .id(user.get().getId())
                .name(user.get().getName())
                .email(user.get().getEmail())
                .build();
    }

}
