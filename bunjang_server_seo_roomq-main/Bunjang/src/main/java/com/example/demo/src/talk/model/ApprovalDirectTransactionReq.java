package com.example.demo.src.talk.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class ApprovalDirectTransactionReq {

    private int directTransactionId;
    private int sendUserIdx;
    private int receiveUserIdx;

}
