package com.example.demo.src.product.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class GetThirdCategoryRes {

    private String thirdCategoryName;
    private int thirdCategoryId;

}
