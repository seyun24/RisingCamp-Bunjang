package com.example.demo.src.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class PatchUserStoreInfoReq {

    private String storeProfileImgUrl;
    private String contactTimeStart;
    private String contactTimeFinish;
    private String storeAddress;
    private String storeName;
    private String storeInfo;
    private String notice;
    private String info;

}
