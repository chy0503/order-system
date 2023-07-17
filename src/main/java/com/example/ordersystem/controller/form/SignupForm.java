package com.example.ordersystem.controller.form;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupForm {
    private String id;
    private String password;
    private String email;
}
