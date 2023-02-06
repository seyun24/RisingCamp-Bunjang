package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.src.store.model.PostFollowReq;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class StoreService {

    private final StoreDao storeDao;
    private final StoreProvider storeProvider;
    private final JwtService jwtService;

    @Autowired
    public StoreService(StoreDao storeDao, StoreProvider storeProvider, JwtService jwtService) {
        this.storeDao = storeDao;
        this.storeProvider = storeProvider;
        this.jwtService = jwtService;
    }


    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public String follow(int storeId, int userId)throws BaseException {
        try {
            if(!(storeDao.getFollowStatus(storeId,userId))) {
                storeDao.follow(storeId,userId);
                return "팔로잉";
            }
            else {
                storeDao.unfollow(storeId,userId);
                return "팔로우";
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
