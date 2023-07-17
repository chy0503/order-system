package com.example.ordersystem.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class OrderDto {
    private Long id;
    private String memberId;
    private Long productId;
    private int productNum;
    private Long addressId;
    private Date orderDate;
    private Date confirmDate;
}
