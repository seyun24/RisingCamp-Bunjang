package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetDirectTradeInfoRes {
    private GetProductInfoRes productInfo;
    private String tradeMethod;
    private int orderId;
    private String orderDate;
    private String paymentMethod;
    private String userName;
    private String phone;
    private int totalPrice;
    private int pointScore;
    private int fee;
}
