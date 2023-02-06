package com.example.demo.src.order.model;

import com.example.demo.src.address.model.GetAddressRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetDirectOrderRes {
    private String url1;
    private String productName;
    private int price;
    private int pointScore;
    private int fee;
}
