package com.example.demo.src.product.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class GetThirdCategoryReq {

    private String firstCategoryName;
    private String secondCategoryName;

}
