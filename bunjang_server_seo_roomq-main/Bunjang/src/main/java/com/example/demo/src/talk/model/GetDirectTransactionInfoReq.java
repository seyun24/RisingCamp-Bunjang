package com.example.demo.src.talk.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class GetDirectTransactionInfoReq {

    private int directTransactionId;
}
