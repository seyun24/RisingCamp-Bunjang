package com.example.demo.src.user.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class GetUserMyRes {

    private String storeProfileImgUrl;
    private String storeName;
    private int bookmarkCnt;
    private List<GetUserMyProductsRes> getUserMyProductsResList;
}
