package com.example.ordersystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartWithProductDto {
    private Long id;
    private String memberId;
    private Long productId;
    private String productName;
    private String productDetail;
    private Long productPrice;
    private int num;

    public static CartWithProductDto from(CartDto cart, ProductDto product) {
        return CartWithProductDto.builder()
                .id(cart.getId())
                .memberId(cart.getMemberId())
                .productId(cart.getProductId())
                .productName(product.getName())
                .productDetail(product.getDetail())
                .productPrice(product.getPrice())
                .num(cart.getNum())
                .build();
    }
}
