package com.example.demo.src.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class GetUserBlockStoresRes {

    private int storeId;
    private String storeProfileImgUrl;
    private String storeName;
    private String blockTime;

}
