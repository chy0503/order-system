package com.example.ordersystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartDto {
    private Long id;
    private String memberId;
    private Long productId;
    private int num;
}
