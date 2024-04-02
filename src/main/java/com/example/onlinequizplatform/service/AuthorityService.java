package com.example.onlinequizplatform.service;

import com.example.onlinequizplatform.dto.AuthorityDto;
import com.example.onlinequizplatform.models.Authority;

import java.util.Optional;

public interface AuthorityService {
    AuthorityDto getRoles(String role);
}
