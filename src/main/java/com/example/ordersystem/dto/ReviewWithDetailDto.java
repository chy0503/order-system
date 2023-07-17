package com.example.ordersystem.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ReviewWithDetailDto {
    private Long id;
    private String memberId;
    private Long orderId;
    private String content;
    private int rating;
    private Date writeDate;
    private Long productId;
    private String productName;
    private String productDetail;

    public static ReviewWithDetailDto from(ReviewDto review, ProductDto product) {
        return ReviewWithDetailDto.builder()
                .id(review.getId())
                .memberId(review.getMemberId())
                .orderId(review.getOrderId())
                .productId(product.getId())
                .productName(product.getName())
                .productDetail(product.getDetail())
                .content(review.getContent())
                .rating(review.getRating())
                .build();
    }
}
