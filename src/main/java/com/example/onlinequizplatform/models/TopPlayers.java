package com.example.onlinequizplatform.models;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TopPlayers {
    private Long id;
    private String userName;
    private BigDecimal score;
    private Long userId;
    private Integer position;
}
