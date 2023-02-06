package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.src.order.model.*;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class OrderProvider {

    private final OrderDao orderDao;
    private final JwtService jwtService;

    @Autowired
    public OrderProvider(OrderDao orderDao, JwtService jwtService) {
        this.orderDao = orderDao;
        this.jwtService = jwtService;
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public List<GetOrderRes> getOrders(int userId)throws BaseException{
  try {
    return orderDao.getOrders(userId);
}catch (Exception exception){
    throw new BaseException(DATABASE_ERROR);
}


    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public List<GetOrderRes> getOrdersCancel(int userId)throws BaseException {
        try {
            return orderDao.getOrdersCancel(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public List<GetOrderRes> getOrdersProgress(int userId)throws BaseException {
        try {
            return orderDao.getOrdersProgress(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public List<GetOrderRes> getOrdersComplete(int userId)throws BaseException {
        try {
            return orderDao.getOrdersComplete(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public GetOrderParcelRes getOrderParcel(int productId, int userId)throws BaseException{
        try {
            if (!orderDao.checkProduct(productId))
                throw new BaseException(INVALID_PRODUCT);
            return orderDao.getOrderParcel(productId,userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public GetDirectOrderRes getDirectOrder(int productId, int userId)throws BaseException{
        try {
            if (!orderDao.checkProduct(productId))
                throw new BaseException(INVALID_PRODUCT);
            return orderDao.getDirectOrder(productId,userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public GetTradeInfoRes getTradeInfo(int productId, int userId)throws BaseException{
        if (!orderDao.checkProduct(productId))
            throw new BaseException(INVALID_PRODUCT);
        if (orderDao.checkTradeMethod(productId, userId).equals("직거래"))
            throw new BaseException(INVALID_ORDER);
        if (!orderDao.checkOrder(productId, userId))
            throw new BaseException(DUPLICATED_ORDER);
        try {
            return orderDao.getTradeInfo(productId, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public GetDirectTradeInfoRes getDirectTradeInfo(int productId, int userId)throws BaseException{
        if (!orderDao.checkProduct(productId))
            throw new BaseException(INVALID_PRODUCT);
        if (orderDao.checkTradeMethod(productId, userId).equals("택배거래"))
            throw new BaseException(INVALID_ORDER);
        if (!(orderDao.checkOrder(productId, userId)))
            throw new BaseException(DUPLICATED_ORDER);
        try {
            return orderDao.getDirectTradeInfo(productId, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
