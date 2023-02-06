package com.example.demo.src.talk.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class GetDirectTransactionInfoRes {

    private int productId;
    private String productImgUrl;
    private String productName;
    private String price;
    private String transactionDate;
    private String transactionLocation;

    private String sendUserStoreName;
    private String sendUserPhoneNum;
    private String receiveUserStoreName;
    private String receiveUserPhoneNum;

}
