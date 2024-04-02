package com.example.onlinequizplatform.models;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Authority {
    private Long id;
    private String role;
}
