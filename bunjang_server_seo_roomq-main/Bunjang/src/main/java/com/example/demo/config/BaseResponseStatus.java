package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    INVALID_ORDER(false,2004,"유효하지 않은 주문입니다."),

    // users
    USERS_LENGTH_USER_NUMBER(false, 2009, "6자리 OTP를 입력해주세요"),
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    POST_USERS_EXISTS_NUMBER(false,2018,"인증번호가 올바르지 않습니다."),
    POST_STORE_EMPTY_NAME(false,2030,"상점명을 입력하세요."),

    //[POST] /Products
    POST_PRODUCT_EMPTY_IMG(false,2019,"첫번째 사진은 필수로 존재해야합니다."),
    POST_PRODUCT_EMPTY_NAME(false,2020,"상품명을 입력해주세요."),
    POST_PRODUCT_EMPTY_FIRSTCATEGORYNAME(false,2021,"첫번째 카테고리를 입력해주세요."),
    POST_PRODUCT_EMPTY_DELIVERYTIP(false,2022,"배송지포함 여부를 입력해주세요."),
    POST_PRODUCT_EMPTY_COUNT(false,2023,"상품의 수량을 입력해주세요."),
    POST_PRODUCT_EMPTY_STATUS(false,2024,"중고상품인지 세상품인지 productStatus를 입력해주세요."),
    POST_PRODUCT_EMPTY_TRADE(false,2025,"교환 가능 여부를 입력해주세요."),
    POST_PRODUCT_EMPTY_DESCRIPTION(false,2026,"상품 설명을 입력해주세요."),
    POST_PRODUCT_EMPTY_SAFEPAY(false,2027,"번개페이 적용여부를 입력하세요."),

    // [GET] /Products
    INVALID_FIRSTCATEGORYID(false,2028,"첫번째 카테고리는 1이상 28이하의 자연수를 입력해주세요."),
    INVALID_SECONDCATEGORYID(false,2029,"두번째 카테고리는 14이상 208이하의 자연수를 입력세주세요."),


    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),


    // [POST] /accounts
    EXCESS_ACCOUNT(false,3015,"계좌는 최대 2개까지 등록 가능합니다."),
    //[DELETE] /accounts
    INVALID_ACCOUNT(false,3016,"존재하지 않는 계좌입니다."),


    // [GET] /products
    INVALID_USER(false,3017,"존재하지 않는 유저입니다."),
    INVALID_PRODUCT(false,3018,"존재하지 않는 상품입니다."),
    DUPLICATED_ORDER(false,3015,"중복된 주문입니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    LOGIN_FAIL(false,4002, "유효하지 않은 회원입니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERINFO(false,4014,"유저정보 수정 실패"),

    MODIFY_FAIL_STOREINFO(false, 4015,"상점 정보 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
