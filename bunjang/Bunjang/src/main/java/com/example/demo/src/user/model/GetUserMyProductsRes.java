package com.example.demo.src.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class GetUserMyProductsRes {

    private int productIdx;
    private String productImgUrl;
    private String productName;
    private String price;
    private String timeDiff;

}
