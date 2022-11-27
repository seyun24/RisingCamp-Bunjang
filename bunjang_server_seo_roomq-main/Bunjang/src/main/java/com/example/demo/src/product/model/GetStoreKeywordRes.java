package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreKeywordRes {
    private String storeName;
    private String storeProfileImgUrl;
    private int storeId;
    private int follower;
    private int productCnt;
}
