package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProductInfoRes {
    private String url1;
    private String productName;
    private int price;
    private String storeName;
}
