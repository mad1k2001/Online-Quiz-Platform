package com.example.onlinequizplatform.models;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Category {
    private Long id;
    private String name;
}
