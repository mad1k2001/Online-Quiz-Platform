package com.example.onlinequizplatform.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String roleId;
}
