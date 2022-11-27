package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class PostOrderReq {
    private int userId;
    private int productId;
    private int pointScore;
    private String paymentMethod;
    private String address;
    private String addressReq;
}


