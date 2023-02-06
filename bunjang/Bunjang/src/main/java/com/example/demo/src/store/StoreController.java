package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.GetThirdCategoryRes;
import com.example.demo.src.store.model.PostFollowReq;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/bunjang/stores")
public class StoreController {

    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final StoreService storeService;
    @Autowired
    private final StoreProvider storeProvider;

    public StoreController(JwtService jwtService, StoreService storeService, StoreProvider storeProvider) {
        this.jwtService = jwtService;
        this.storeService = storeService;
        this.storeProvider = storeProvider;
    }

    @ResponseBody
    @PostMapping("/followers/{storeId}/{userId}")
    public BaseResponse follow(@PathVariable("storeId") int storeId,@PathVariable("userId")int userId){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            return new BaseResponse<>(storeService.follow(storeId,userId));

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/followers-cnt/{userId}")
    public BaseResponse getFollower(@PathVariable("userId")int userId){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            return new BaseResponse<>(storeProvider.getFollower(userId));

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/followings-cnt/{userId}")
    public BaseResponse getFollowing(@PathVariable("userId")int userId){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            };
            return new BaseResponse<>(storeProvider.getFollowing(userId));

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
