package com.example.demo.src.talk.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class PostTalkReq {

    private int sendUserIdx;
    private int receiveUserIdx;
    private String contents;

}
