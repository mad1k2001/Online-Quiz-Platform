package com.example.onlinequizplatform.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TopPlayers {
    private Long id;
    private String userName;
    private BigDecimal score;
    private Long userId;
    private Integer position;
}
