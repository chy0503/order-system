package com.example.ordersystem.controller.form;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class cartForm {
    private String memberId;
    private String productId;
}
