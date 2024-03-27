package com.example.onlinequizplatform.models;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Quiz {
    private Long id;
    private String title;
    private String description;
    private Long creatorId;
}
