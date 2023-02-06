package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.src.order.model.PostOrderReq;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.DUPLICATED_ORDER;

@Service
public class OrderService {

    private final OrderProvider orderProvider;
    private final OrderDao orderDao;
    private final JwtService jwtService;

    @Autowired
    public OrderService(OrderProvider orderProvider, OrderDao orderDao, JwtService jwtService) {
        this.orderProvider = orderProvider;
        this.orderDao = orderDao;
        this.jwtService = jwtService;
    }


    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public void postParcelOrder(PostOrderReq postOrderReq)throws BaseException {
        if (orderDao.checkOrder(postOrderReq.getProductId(), postOrderReq.getUserId()))
            throw new BaseException(DUPLICATED_ORDER);
        try {
                orderDao.postParcelOrder(postOrderReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public void postDirectOrder(PostOrderReq postOrderReq)throws BaseException {
        if (orderDao.checkOrder(postOrderReq.getProductId(), postOrderReq.getUserId()))
            throw new BaseException(DUPLICATED_ORDER);
        try {
            orderDao.postDirectOrder(postOrderReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
