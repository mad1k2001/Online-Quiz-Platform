package com.example.onlinequizplatform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {
    @NotBlank(message = "field not filled in name")
    private String name;
    @NotBlank(message = "field not filled in email")
    @Email(message = "wrong format email")
    private String email;
    @NotBlank(message = "field not filled in password")
    private String password;
}
