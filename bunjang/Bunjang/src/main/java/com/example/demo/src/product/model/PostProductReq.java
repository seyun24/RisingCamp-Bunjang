package com.example.demo.src.product.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class PostProductReq {

    // 상품 정보들 넣기
    private String productName;

    private String firstCategoryName;
    private String secondCategoryName;
    private String thirdCategoryName;

    private int price;
    private String deliveryTip;
    private int count;
    private String productStatus;
    private String trade;
    private String location;
    private String description;
    private String safePay;



    // 이미지 url 넣기
    private String url1;
    private String url2;
    private String url3;
    private String url4;
    private String url5;
    private String url6;
    private String url7;
    private String url8;
    private String url9;
    private String url10;
    private String url11;
    private String url12;

    // 태그 단어 넣기
    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;

}
