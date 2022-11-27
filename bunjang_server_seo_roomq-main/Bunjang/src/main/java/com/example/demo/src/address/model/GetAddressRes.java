package com.example.demo.src.address.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAddressRes {
    private String status;
    private String addressName;
    private String address;
    private String detailAddress;
    private String phone;
}
