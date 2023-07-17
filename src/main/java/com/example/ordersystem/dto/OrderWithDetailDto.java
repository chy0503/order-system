package com.example.ordersystem.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class OrderWithDetailDto {
    private Long id;
    private String memberId;
    private Long productId;
    private String productName;
    private Long productPrice;
    private int productNum;
    private Long totalPrice;
    private Long addressId;
    private String addressName;
    private Date orderDate;
    private Date confirmDate;
    private boolean isReviewed;

    public static OrderWithDetailDto from(OrderDto order, ProductDto product, AddressDto addr, boolean isReview) {
        return OrderWithDetailDto.builder()
                .id(order.getId())
                .memberId(order.getMemberId())
                .productId(order.getProductId())
                .productName(product.getName())
                .productPrice(product.getPrice())
                .productNum(order.getProductNum())
                .totalPrice(product.getPrice() * order.getProductNum())
                .addressId(order.getAddressId())
                .addressName(addr.getName())
                .orderDate(order.getOrderDate())
                .confirmDate(order.getConfirmDate())
                .isReviewed(isReview)
                .build();
    }
}
