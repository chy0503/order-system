package com.example.ordersystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private String detail;
    private Long price;
    private float rating;
}
