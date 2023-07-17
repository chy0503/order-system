package com.example.ordersystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDto {
    private String id;
    private String pwd;
    private String email;
}
