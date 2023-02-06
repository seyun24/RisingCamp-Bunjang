package com.example.demo.src.bookmark.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class GetUserBookmarksRes {

    private int productIdx;
    private String safePay;
    private String productImgUrl;
    private String productName;
    private String price;
    private String storeProfileImgUrl;
    private String storeName;
    private String timeDiff;

}
