package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor

public class GetMainProductsRes {

    private int productId;
    private String productImgUrl;
    private String price;
    private String productName;
    private String location;
    private String timeDiff;
    private String safePay;
    private int bookmarkCnt;

}
