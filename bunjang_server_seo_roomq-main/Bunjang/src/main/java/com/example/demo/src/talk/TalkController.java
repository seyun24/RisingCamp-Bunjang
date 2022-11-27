package com.example.demo.src.talk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.talk.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/bunjang/talks")

public class TalkController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private  final TalkProvider talkProvider;
    @Autowired
    private final TalkService talkService;
    @Autowired
    private final JwtService jwtService;


    public TalkController(TalkProvider talkProvider, TalkService talkService, JwtService jwtService) {
        this.talkProvider = talkProvider;
        this.talkService = talkService;
        this.jwtService = jwtService;
    }

    /**
     * 번개톡 메세지 보내기
     * [POST]
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> postTalk(@RequestBody PostTalkReq postTalkReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            int roomId = talkService.postTalk(postTalkReq);

            String result = "성공적으로 전송되었습니다. ( roomIdx : " + roomId +" )";

            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 번개톡 직거래 요청
     * [POST] /directTransaction
     */
    @ResponseBody
    @PostMapping("/directTransaction")
    public BaseResponse<String> postDirectTransaction(@RequestBody PostDirectTransactionReq postDirectTransactionReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            int directTransactionId = talkService.postDirectTransaction(postDirectTransactionReq);

            String result = "직거래를 성공적으로 요청하였습니다. " + "직거래 id는 " +  directTransactionId +"입니다.";

            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 직거래 요청 수정
     * [PATCH] /directTransaction
     */
    @ResponseBody
    @PatchMapping("/directTransaction")
    public BaseResponse<String> modifyDirectTransaction(@RequestBody ModifyDirectTransactionReq modifyDirectTransactionReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            talkService.modifyDirectTransaction(modifyDirectTransactionReq);
            String result = "직거래의 정보를 성공적으로 수정하였습니다.";

            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 직거래 요청 승인
     * [POST] /directTransaction/approval
     */
    @ResponseBody
    @PostMapping("/directTransaction/approval")
    public BaseResponse<String> approvalDirectTransaction(@RequestBody ApprovalDirectTransactionReq approvalDirectTransactionReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            talkService.approvalDirectTransaction(approvalDirectTransactionReq);
            String result = "직거래 요청이 승인되어 거래가 약속되었습니다.";

            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 직거래 상세 정보 조회
     * [GET] /directTransaction/detail
     */
    @ResponseBody
    @GetMapping("/directTransaction/detail")
    public BaseResponse<GetDirectTransactionInfoRes> getDirectTransactionInfo(@RequestBody GetDirectTransactionInfoReq getDirectTransactionInfoReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            GetDirectTransactionInfoRes getDirectTransactionInfoRes = talkProvider.getDirectTransactionInfo(getDirectTransactionInfoReq);

            return new BaseResponse<>(getDirectTransactionInfoRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 직거래 완료
     * [POST] /directTransaction/done
     */
    @ResponseBody
    @PostMapping("/directTransaction/done")
    public BaseResponse<String> directTransactionDone(@RequestBody ApprovalDirectTransactionReq approvalDirectTransactionReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            talkService.directTransactionDone(approvalDirectTransactionReq);
            String result = "직거래가 완료되었습니다. ";

            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 번개톡 대화 내용 조회
     * [GET] /:roomIdx
     */
    @ResponseBody
    @GetMapping("/{roomIdx}")
    public BaseResponse<List<GetUserChatsRes>> getUserChats(@PathVariable("roomIdx") int roomIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            List<GetUserChatsRes> getUserChatsRes = talkProvider.getUserChats(roomIdx);

            return new BaseResponse<>(getUserChatsRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
