package com.example.demo.src.product.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class GetSecondCategoryRes {

    private String secondCategoryName;
    private int secondCategoryId;
    private int thirdCategoryExist;

}
