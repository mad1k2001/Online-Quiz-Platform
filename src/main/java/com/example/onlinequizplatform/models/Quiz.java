package com.example.onlinequizplatform.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class Quiz {
    private Long id;
    private String title;
    private String description;
    private Long creatorId;

}
