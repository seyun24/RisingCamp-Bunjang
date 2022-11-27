package com.example.demo.src.order.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor

public class GetTradeInfoRes {
    private GetProductInfoRes productInfo;
    private String tradeMethod;
    private int orderId;
    private String orderDate;
    private String paymentMethod;
    private String userName;
    private String phone;
    private String address;
    private int totalPrice;
    private int pointScore;
    private int fee;
    private String deliveryTip;
}
