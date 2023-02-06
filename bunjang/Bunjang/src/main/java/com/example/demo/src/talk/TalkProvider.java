package com.example.demo.src.talk;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.talk.model.*;
import com.example.demo.utils.AES128;
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

public class TalkProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TalkDao talkDao;
    private final JwtService jwtService;

    @Autowired
    public TalkProvider(TalkDao talkDao, JwtService jwtService) {
        this.talkDao = talkDao;
        this.jwtService = jwtService;
    }

    // 이미 만들어진 채팅방 존재 여부 확인
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public int checkRoomExist(int sendUserIdx, int receiveUserIdx) throws BaseException {
        try {
            int result = talkDao.checkRoomExist(sendUserIdx,receiveUserIdx);

            return result;

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 직거래 상세 정보 조회
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public GetDirectTransactionInfoRes getDirectTransactionInfo(GetDirectTransactionInfoReq getDirectTransactionInfoReq) throws BaseException {
        try {
            GetDirectTransactionInfoRes getDirectTransactionInfoRes = talkDao.getDirectTransactionInfo(getDirectTransactionInfoReq);
            return getDirectTransactionInfoRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 번개톡 대화 내용 조회
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public List<GetUserChatsRes> getUserChats(int roomIdx) throws BaseException{
        try {
            List<GetUserChatsRes> getUserChatsRes = talkDao.getUserChats(roomIdx);

            return getUserChatsRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
