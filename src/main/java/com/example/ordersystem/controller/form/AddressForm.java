package com.example.ordersystem.controller.form;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressForm {
    private String name;
    private String memberId;
    private String address;
    private String phone;
}
