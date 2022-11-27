package com.example.demo.src.talk.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class GetUserChatsRes {

    private int sendUserIdx;
    private int receiveUserIdx;
    private String contents;
    private String detailContents;
    private String productName;
    private String method;
    private String price;
    private String postTime;


}
