package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.GetTagsRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class StoreProvider {

    private final JwtService jwtService;
    private final StoreDao storeDao;

    @Autowired
    public StoreProvider(JwtService jwtService, StoreDao storeDao) {
        this.jwtService = jwtService;
        this.storeDao = storeDao;
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public int getFollower(int userId) throws BaseException{
        try {
            return storeDao.getFollower(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public int getFollowing(int userId) throws BaseException{
        try {
            return storeDao.getFollowing(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
