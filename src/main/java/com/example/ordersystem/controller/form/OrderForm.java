package com.example.ordersystem.controller.form;

import ch.qos.logback.core.joran.action.AppenderRefAction;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderForm {
    private String memberId;
    private Long productId;
    private String productName;
    private Long productPrice;
    private int productNum;
    private Long totalPrice;
    private Long addressId;
    private Long inCart;
}
