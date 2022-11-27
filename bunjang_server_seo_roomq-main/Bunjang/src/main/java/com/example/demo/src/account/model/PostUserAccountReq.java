package com.example.demo.src.account.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class PostUserAccountReq {

    private String accountHolder;
    private int bankId;
    private String accountNum;
    private String status;
}
