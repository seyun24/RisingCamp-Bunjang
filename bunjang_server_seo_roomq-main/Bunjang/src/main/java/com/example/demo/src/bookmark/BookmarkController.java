package com.example.demo.src.bookmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.bookmark.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController

@RequestMapping("/bunjang/bookmarks")


public class BookmarkController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final BookmarkProvider bookmarkProvider;
    @Autowired
    private final BookmarkService bookmarkService;
    @Autowired
    private final JwtService jwtService;

    public BookmarkController(BookmarkProvider bookmarkProvider, BookmarkService bookmarkService,JwtService jwtService) {
        this.bookmarkProvider = bookmarkProvider;
        this.bookmarkService = bookmarkService;
        this.jwtService = jwtService;
    }

    /**
     * 상품 찜/찜 취소
     * [POST] /:userIdx/:productIdx
     */
    @ResponseBody
    @PostMapping("/{userIdx}/{productIdx}")
    public BaseResponse<String> createPatchBookmark(@PathVariable("userIdx") int userIdx, @PathVariable("productIdx") int productIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            String result = bookmarkService.createPatchBookmark(userIdx,productIdx);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 찜한 상품 조회
     * [GET] /:userIdx
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetUserBookmarksRes>> getUserBookmarks(@PathVariable("userIdx") int userIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetUserBookmarksRes> getUserBookmarksRes = bookmarkProvider.getUserBookmarks(userIdx);

            return new BaseResponse<>(getUserBookmarksRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저가 상품을 찜한 상태인지 여부
     * [GET] /:userIdx/:productIdx
     */
    @ResponseBody
    @GetMapping("/{userIdx}/{productIdx}")
    public BaseResponse existBookmark(@PathVariable("userIdx") int userIdx, @PathVariable("productIdx") int productIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            int result = bookmarkProvider.existBookmark(userIdx,productIdx);

            return new BaseResponse(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }



}
