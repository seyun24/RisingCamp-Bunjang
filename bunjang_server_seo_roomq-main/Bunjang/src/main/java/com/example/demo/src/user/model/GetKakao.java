package com.example.demo.src.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class GetKakao {

    private String result;
    private int userIdx;
    private String jwt;
}
