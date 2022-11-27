package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;


import static com.example.demo.config.BaseResponseStatus.*;

@RestController // Rest API 또는 WebAPI를 개발하기 위한 어노테이션. @Controller + @ResponseBody 를 합친것.
                // @Controller      [Presentation Layer에서 Contoller를 명시하기 위해 사용]
                //  [Presentation Layer?] 클라이언트와 최초로 만나는 곳으로 데이터 입출력이 발생하는 곳
                //  Web MVC 코드에 사용되는 어노테이션. @RequestMapping 어노테이션을 해당 어노테이션 밑에서만 사용할 수 있다.
                // @ResponseBody    모든 method의 return object를 적절한 형태로 변환 후, HTTP Response Body에 담아 반환.
@RequestMapping("/bunjang/users")
// method가 어떤 HTTP 요청을 처리할 것인가를 작성한다.
// 요청에 대해 어떤 Controller, 어떤 메소드가 처리할지를 맵핑하기 위한 어노테이션
// URL(/app/users)을 컨트롤러의 메서드와 매핑할 때 사용
/**
 * Controller란?
 * 사용자의 Request를 전달받아 요청의 처리를 담당하는 Service, Prodiver 를 호출
 */
public class UserController {
    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************

    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.

    @Autowired  // 객체 생성을 스프링에서 자동으로 생성해주는 역할. 주입하려 하는 객체의 타입이 일치하는 객체를 자동으로 주입한다.
    // IoC(Inversion of Control, 제어의 역전) / DI(Dependency Injection, 의존관계 주입)에 대한 공부하시면, 더 깊이 있게 Spring에 대한 공부를 하실 수 있을 겁니다!(일단은 모르고 넘어가셔도 무방합니다.)
    // IoC 간단설명,  메소드나 객체의 호출작업을 개발자가 결정하는 것이 아니라, 외부에서 결정되는 것을 의미
    // DI 간단설명, 객체를 직접 생성하는 게 아니라 외부에서 생성한 후 주입 시켜주는 방식
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService) {
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!
    }


    @ResponseBody
    @PostMapping("/log-in")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq) {
        try {

            PostLoginRes postLoginRes = userService.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/sigh-up")
    public BaseResponse<PostLoginRes> sighIn(@RequestBody PostLoginReq postLoginReq) {
        try {

            PostLoginRes postLoginRes = userService.sighIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/checks/{phone}")
    public BaseResponse checkAccount(@PathVariable("phone") String phone){
        try {

            int result = userProvider.checkAccount(phone);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }



    @PostMapping("/checks/send-sms")
    @ResponseBody
    public BaseResponse sendSMS(@RequestBody PostAuthNumReq postAuthNumReq) {

        Random rand  = new Random();
        String numStr = "";
        for(int i=0; i<6; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr+=ran;
        }
        System.out.println("수신자 번호 : " + postAuthNumReq.getPhone());
        System.out.println("인증번호 : " + numStr);
        userService.certifiedPhoneNumber(postAuthNumReq.getPhone(),numStr);
        postAuthNumReq.setNumber(numStr);
        try {
            userService.createAuth(postAuthNumReq);
            String result="발신 성공";
            return new BaseResponse<>(result);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @DeleteMapping("/checks/send-sms")
    @ResponseBody
    public BaseResponse deleteAuth(@RequestBody PostAuthNumReq postAuthNumReq){
        try {
            userService.deleteAuth(postAuthNumReq);
            String result="삭제 성공";
            return new BaseResponse<>(result);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/checks/send-sms/{phone}/{number}")
    @ResponseBody
    public BaseResponse checkAuth(@PathVariable("phone") String phone,@PathVariable("number") String number){
        if(number.length()!=6) return new BaseResponse<>(USERS_LENGTH_USER_NUMBER);
        try {
            if(userProvider.checkNum(phone, number)!=1)
                return new BaseResponse<>(POST_USERS_EXISTS_NUMBER);
            return new BaseResponse<>("성공");
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 상점 정보 수정
     * [PATCH] /:userIdx/storeInfo
     */
    @ResponseBody
    @PatchMapping("/{userIdx}/storeInfo")
    public BaseResponse<String> modifyStoreInfo(@PathVariable("userIdx") int userIdx,@RequestBody PatchUserStoreInfoReq patchUserStoreInfoReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if (patchUserStoreInfoReq.getStoreName() == null) {
                return new BaseResponse<>(POST_STORE_EMPTY_NAME);
            }
            userService.modifyUserStoreInfo(userIdx, patchUserStoreInfoReq);

            String result = "상점 정보가 변경되었습니다.";

            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저 정보 수정(계정 설정)
     * [PATCH] /:userIdx/userInfo
     */
    @ResponseBody
    @PatchMapping("/{userIdx}/userInfo")
    public BaseResponse<String> modifyUserInfo(@PathVariable("userIdx") int userIdx,@RequestBody PatchUserInfoReq patchUserInfoReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            userService.modifyUserInfo(userIdx, patchUserInfoReq);

            String result = "유저 정보가 변경되었습니다.";

            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 상점 차단 하기
     * [POST] /:userIdx/:storeIdx/new-block
     */
    @ResponseBody
    @PostMapping("/{userIdx}/{storeIdx}/new-block")
    public BaseResponse<String> createBlockStore(@PathVariable("userIdx") int userIdx,@PathVariable("storeIdx") int storeIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            userService.createBlockStore(userIdx,storeIdx);

            String result = "가게" + storeIdx + "을(를) 차단했습니다.";

            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 차단 상점 조회
     * [GET] /:userIdx/blockStores
     */
    @ResponseBody
    @GetMapping("/{userIdx}/blockStores")
    public BaseResponse<List<GetUserBlockStoresRes>> getUserBlockStores(@PathVariable("userIdx") int userIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetUserBlockStoresRes> getUserBlockStoresRes = userProvider.getUserBlockStores(userIdx);

            return new BaseResponse<>(getUserBlockStoresRes);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 최근 본 상품 조회
     * [GET] /:userIdx/history
     */
    @ResponseBody
    @GetMapping("/{userIdx}/history")
    public BaseResponse<List<GetUserHistoryProductRes>> getUserHistoryProduct(@PathVariable("userIdx") int userIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetUserHistoryProductRes> getUserHistoryProductRes = userProvider.getUserHistoryProduct(userIdx);

            return new BaseResponse<>(getUserHistoryProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 마이 페이지 조회
     * [GET] /:userIdx/my
     */
    @ResponseBody
    @GetMapping("/{userIdx}/my")
    public BaseResponse<GetUserMyRes> getUserMy(@PathVariable("userIdx") int userIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            GetUserMyRes getUserMyRes= userProvider.getUserMy(userIdx);

            return new BaseResponse<>(getUserMyRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 카카오 소셜 로그인/회원가입
     * [GET] /kakao
     */
    @ResponseBody
    @GetMapping("/kakao")
    public BaseResponse<GetKakao>  kakaoCallback(@RequestParam String code) {
        try {
            String access_Token = userService.getKaKaoAccessToken(code);
            String email = userService.createKakaoUser(access_Token);

            GetKakao getKakao = userService.connectKakao(email);

            return new BaseResponse<>(getKakao);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
