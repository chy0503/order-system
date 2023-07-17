package com.example.ordersystem.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ReviewDto {
    private Long id;
    private String memberId;
    private Long orderId;
    private String content;
    private int rating;
    private Date writeDate;
}
