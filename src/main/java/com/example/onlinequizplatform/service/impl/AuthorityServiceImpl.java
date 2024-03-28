package com.example.onlinequizplatform.service.impl;

import com.example.onlinequizplatform.dao.AuthorityDao;
import com.example.onlinequizplatform.dto.AuthorityDto;
import com.example.onlinequizplatform.models.Authority;
import com.example.onlinequizplatform.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorityServiceImpl implements AuthorityService {
 private AuthorityDao authorityDao;


    @Override
    public AuthorityDto getRoles(String role) {
        Optional<Authority> authority = authorityDao.getRoles(role);
        return AuthorityDto.builder()
                .id(authority.get().getId())
                .role(authority.get().getRole())
                .build();
    }
}
