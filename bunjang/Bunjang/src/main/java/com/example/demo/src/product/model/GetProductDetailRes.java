package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor

public class GetProductDetailRes {

    private GetProductImgRes getProductImgRes;
    private int productId;
    private String price;
    private String safePay;
    private String productName;
    private String location;
    private String timeDiff;
    private String productStatus;
    private int count;
    private String deliveryTip;
    private String trade;
    private String description;
    private String categoryName;
    private int bookmarkCnt;
    private GetProductTagRes getProductTagRes;

}
