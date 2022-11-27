package com.example.demo.src.user;

import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;

import static com.example.demo.config.BaseResponseStatus.*;

/**
 * Service란?
 * Controller에 의해 호출되어 실제 비즈니스 로직과 트랜잭션을 처리: Create, Update, Delete 의 로직 처리
 * 요청한 작업을 처리하는 관정을 하나의 작업으로 묶음
 * dao를 호출하여 DB CRUD를 처리 후 Controller로 반환
 */
@Service    // [Business Layer에서 Service를 명시하기 위해서 사용] 비즈니스 로직이나 respository layer 호출하는 함수에 사용된다.
            // [Business Layer]는 컨트롤러와 데이터 베이스를 연결
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log 처리부분: Log를 기록하기 위해 필요한 함수입니다.

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!


    @Autowired //readme 참고
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    }


    // 회원정보 수정(Patch)
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public void modifyUserInfo(int userIdx, PatchUserInfoReq patchUserInfoReq) throws BaseException {
        try {
            int result = userDao.modifyUserInfo(userIdx, patchUserInfoReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USERINFO);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 회원 상점 정보 수정
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public void modifyUserStoreInfo(int userIdx, PatchUserStoreInfoReq patchUserStoreInfoReq) throws BaseException {
        try {
            int result = userDao.modifyUserStoreInfo(userIdx,patchUserStoreInfoReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_STOREINFO);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
            try {
                User user=userDao.getUserId(postLoginReq);
                int userId=user.getUserIdx();
                String jwt = jwtService.createJwt(userId);
                return new PostLoginRes(userId,jwt);

            } catch (Exception exception) {
                throw new BaseException(LOGIN_FAIL);
            }
        }


    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public PostLoginRes sighIn(PostLoginReq postLoginReq) throws BaseException{
            try {
                int userIdx = userDao.createUser(postLoginReq);

                String jwt = jwtService.createJwt(userIdx);
                return new PostLoginRes(userIdx,jwt);

            } catch (Exception exception) {
                throw new BaseException(DATABASE_ERROR);
            }
        }


    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public void certifiedPhoneNumber(String phoneNumber, String cerNum) {

        String api_key = "NCSRPJHD6ZZAZPTB";
        String api_secret = "8WCBPFCPUWKOFTRHXEID9BGCZDBE3BLA";
        Message coolsms = new Message(api_key, api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);    // 수신전화번호
        params.put("from", "01046186779");    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "본인확인\n" +
                "인증번호" + "("+cerNum+")" + "입력시\n" +
                "정상처리 됩니다.");
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }

    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public void createAuth(PostAuthNumReq postAuthNumReq)throws BaseException{
        try {
            userDao.createAuth(postAuthNumReq);
        } catch (Exception exception) {
        throw new BaseException(DATABASE_ERROR);
    }
    }

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public void deleteAuth(PostAuthNumReq postAuthNumReq)throws BaseException{
        try {
            userDao.deleteAuth(postAuthNumReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 상점 차단
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public void createBlockStore(int userIdx, int storeIdx) throws BaseException {
        try {
            userDao.createBlockStore(userIdx, storeIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 카카오 소셜로그인 AccessToken 받기
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public String getKaKaoAccessToken (String code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=8f063aa69ba38fa1bcd7ad057a0ca71b"); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri=https://prod.roomq.shop/bunjang/users/kakao"); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    // 카카오 소셜 사용자 정보 받기
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public String createKakaoUser(String token) throws BaseException {

        String reqURL = "https://kapi.kakao.com/v2/user/me";

        //access_token을 이용하여 사용자 정보 조회
        String email = null;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            email = "";
            if (hasEmail) {
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }

            System.out.println("email : " + email);

            br.close();
            return email;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return email;
    }

    // 카카오 로그인/회원 가입
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public GetKakao connectKakao(String email) throws BaseException {
        try {
            if (userProvider.checkKakaoUser(email) == 1) {
                // 존재하면 로그인
                int userIdx = userDao.kakaoLogIn(email);
                String jwt = jwtService.createJwt(userIdx);
                String result = "로그인에 성공했습니다.";
                return new GetKakao(result,userIdx,jwt);
            } else {
                // 존재안하면 회원가입 (userProvider.checkKakaoUser(email) == 0)인 경우
                int userIdx = userDao.kakaoSignUp(email);
                String jwt = jwtService.createJwt(userIdx);
                String result = "회원가입에 성공했습니다.";
                return new GetKakao(result, userIdx,jwt);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
