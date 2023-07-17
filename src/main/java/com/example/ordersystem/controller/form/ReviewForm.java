package com.example.ordersystem.controller.form;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewForm {
    private String memberId;
    private Long orderId;
    private int rating;
    private String content;
}
