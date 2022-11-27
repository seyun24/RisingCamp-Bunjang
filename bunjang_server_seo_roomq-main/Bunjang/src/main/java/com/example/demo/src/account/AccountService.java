package com.example.demo.src.account;

import com.example.demo.config.BaseException;
import com.example.demo.src.account.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static com.example.demo.config.BaseResponseStatus.*;

@Service

public class AccountService {

    private final AccountDao accountDao;
    private final AccountProvider accountProvider;
    private final JwtService jwtService;

    @Autowired
    public AccountService(AccountDao accountDao, AccountProvider accountProvider,JwtService jwtService) {
        this.accountDao = accountDao;
        this.accountProvider = accountProvider;
        this.jwtService = jwtService;
    }

    //계좌 추가

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public void creatUserAccount(int userIdx,PostUserAccountReq postUserAccountReq) throws BaseException{
        // 이미 계좌를 2개 등록한 경우
        if (accountProvider.checkAccountCnt(userIdx) >= 2) {
            throw new BaseException(EXCESS_ACCOUNT);
        }
        try {
            if (accountProvider.checkAccountCnt(userIdx) == 0 ) {
                // 계좌가 없는 경우 무조건 대표계좌  'A' 로 넣기
                accountDao.creatUserAccountForA(userIdx,postUserAccountReq);
            } else if (accountProvider.checkAccountCnt(userIdx) == 1){
                // 계좌가 1개 있는 경우 status에 따라 넣기
                if (postUserAccountReq.getStatus() != null && postUserAccountReq.getStatus().equals("A")) {
                    // A로 넣고 원래 있던 계좌는 D로 바꾸기
                    accountDao.createUserAccountModify(userIdx,postUserAccountReq);
                } else if (postUserAccountReq.getStatus() == null ){
                    // 그냥 D로 넣기
                    accountDao.createUserAccountForD(userIdx,postUserAccountReq);
                } else if (postUserAccountReq.getStatus() != null) {
                    accountDao.createUserAccountForD(userIdx,postUserAccountReq);
                }
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 계좌 삭제
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public void deleteUserAccount(int userIdx, int accountIdx) throws BaseException {
        if ( accountProvider.checkAccountCnt(userIdx) == 0 ) {
            throw new BaseException(INVALID_ACCOUNT);
        }
        try {
            if (accountProvider.checkAccountCnt(userIdx) == 1) {
                // 계좌가 1개만 있을때 그냥 삭제
                accountDao.deleteUserAccountFor1(accountIdx);
            } else {
                // 1개 삭제하고 나머지 한개 A로 바꾸기
                accountDao.deleteUserAccountFor2(userIdx,accountIdx);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}



