package com.example.demo.src.address.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetSelectAddressRes {
    private String address;
    private String detailAddress;
    private String addressName;
    private String phone;
}