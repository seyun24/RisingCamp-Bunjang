package com.example.demo.src.talk;

import com.example.demo.config.BaseException;
import com.example.demo.src.talk.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static com.example.demo.config.BaseResponseStatus.*;

@Service

public class TalkService {

    private final TalkDao talkDao;
    private final TalkProvider talkProvider;
    private final JwtService jwtService;

    @Autowired
    public TalkService(TalkDao talkDao,TalkProvider talkProvider, JwtService jwtService) {
        this.talkDao = talkDao;
        this.talkProvider = talkProvider;
        this.jwtService = jwtService;
    }

    // 번개톡 메세지 보내기
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public int postTalk(PostTalkReq postTalkReq ) throws BaseException {
        int a = 0;
        try {
            if( talkProvider.checkRoomExist(postTalkReq.getSendUserIdx(), postTalkReq.getReceiveUserIdx()) == 0 ) {

                int roomId = talkDao.postTalkNew(postTalkReq);
                return roomId;
            } else if ( talkProvider.checkRoomExist(postTalkReq.getSendUserIdx(), postTalkReq.getReceiveUserIdx()) == 1 ) {

                int roomId = talkDao.postTalk(postTalkReq);
                return roomId;
            } else {
                return a;
            }

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 번개톡 직거래 요청
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public int postDirectTransaction(PostDirectTransactionReq postDirectTransactionReq) throws BaseException {
        try {
            int directTransactionId = talkDao.postDirectTransaction(postDirectTransactionReq);
            return directTransactionId;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 직거래 요청 수정 ( 승인 전 수정 가능)
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public void modifyDirectTransaction(ModifyDirectTransactionReq modifyDirectTransactionReq) throws BaseException {
        try {
            talkDao.modifyDirectTransaction(modifyDirectTransactionReq);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 직거래 요청 승인
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public void approvalDirectTransaction(ApprovalDirectTransactionReq approvalDirectTransactionReq) throws BaseException {
        try {
            talkDao.approvalDirectTransaction(approvalDirectTransactionReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 직거래 완료
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public void directTransactionDone(ApprovalDirectTransactionReq approvalDirectTransactionReq) throws BaseException {
        try {
            talkDao.directTransactionDone(approvalDirectTransactionReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }



}
