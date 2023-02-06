package com.example.demo.src.account.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class GerUserAccountRes {

    private String bankStatus;
    private String bankName;
    private String accountNum;
    private String accountHolder;

}
