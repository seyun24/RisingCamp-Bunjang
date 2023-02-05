package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import static com.example.demo.config.BaseResponseStatus.*;

@Service

public class UserProvider {



    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;



    // 해당 이메일이 이미 User Table에 존재하는지 확인
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public int checkEmail(String email) throws BaseException {
        try {
            return userDao.checkEmail(email);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // User들의 정보를 조회
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public List<GetUserRes> getUsers() throws BaseException {
        try {
            List<GetUserRes> getUserRes = userDao.getUsers();
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 nickname을 갖는 User들의 정보 조회
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public List<GetUserRes> getUsersByNickname(String nickname) throws BaseException {
        try {
            List<GetUserRes> getUsersRes = userDao.getUsersByNickname(nickname);
            return getUsersRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 해당 userIdx를 갖는 User의 정보 조회
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public GetUserRes getUser(int userIdx) throws BaseException {
        try {
            GetUserRes getUserRes = userDao.getUser(userIdx);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public int checkNum(String phone, String num) throws BaseException{
        try {
             return userDao.checkNum(phone, num);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public int checkAccount(String phone) throws BaseException{
        try {
            return userDao.checkAccount(phone);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 차단 상점 조회
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public List<GetUserBlockStoresRes> getUserBlockStores(int userIdx) throws BaseException {
        try {
            List<GetUserBlockStoresRes > getUserBlockStoresRes = userDao.getUserBlockStores(userIdx);
            return getUserBlockStoresRes;

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 최근 본 상품 조회
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public List<GetUserHistoryProductRes> getUserHistoryProduct(int userIdx) throws BaseException {
        if (userDao.checkUser(userIdx) == 0 ) {
            throw new BaseException(INVALID_USER);
        }
        try {
            List<GetUserHistoryProductRes> getUserHistoryProductRes = userDao.getUserHistoryProduct(userIdx);
            return getUserHistoryProductRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 마이페이지 조회
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public GetUserMyRes getUserMy(int userIdx) throws BaseException{
        try {
            GetUserMyRes getUserMyRes = userDao.getUserMy(userIdx);
            return getUserMyRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 카카오 소셜 로그인 이메일 중복 체크
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public int checkKakaoUser(String email) throws BaseException{
        try {
            int result = userDao.checkKakaoUser(email);
            return result;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
