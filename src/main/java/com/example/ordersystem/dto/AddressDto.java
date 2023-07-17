package com.example.ordersystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDto {
    private Long id;
    private String name;
    private String memberId;
    private String address;
    private String phone;
    private int isActive;
}
