package com.example.demo.src.order.model;

import com.example.demo.src.address.model.GetAddressRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetOrderParcelRes {
    private String url1;
    private String productName;
    private int price;
    private GetAddressRes address;
    private int pointScore;
    private int fee;
    private int deliveryFee;
}
