package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostFollowReq {
    private final int storeId;
    private final int userId;
}
