package com.example.onlinequizplatform.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Quiz {
    private Long id;
    private String title;
    private String description;
    private Long creatorId;

}
