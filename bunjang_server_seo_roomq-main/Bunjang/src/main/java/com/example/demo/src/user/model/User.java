package com.example.demo.src.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor

public class User {
    private int userIdx;
    private String userName;
    private String firstNumber;
    private String birthdate;
    private String newsAgency;
    private String phone;
}
