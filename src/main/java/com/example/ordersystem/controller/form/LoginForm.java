package com.example.ordersystem.controller.form;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {
    private String id;
    private String password;
}
