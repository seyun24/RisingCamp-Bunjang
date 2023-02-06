package com.example.demo.src.bookmark;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.bookmark.model.*;
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

public class BookmarkProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BookmarkDao bookmarkDao;
    private final JwtService jwtService;

    @Autowired
    public BookmarkProvider(BookmarkDao bookmarkDao, JwtService jwtService) {
        this.bookmarkDao = bookmarkDao;
        this.jwtService = jwtService;
    }

    // 찜 여부 확인
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public int checkBookmark(int userIdx, int productIdx) throws BaseException {
        try {
            return bookmarkDao.checkBookmark(userIdx,productIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 찜 조회
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public List<GetUserBookmarksRes> getUserBookmarks(int userIdx) throws BaseException {
        if (bookmarkDao.checkUser(userIdx) == 0 ) {
            throw new BaseException(INVALID_USER);
        }
        try {

            List<GetUserBookmarksRes> getUserBookmarksRes = bookmarkDao.getUserBookmarks(userIdx);

            return getUserBookmarksRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 유저가 상품을 찜했는지 여부
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public int existBookmark(int userIdx, int productIdx) throws BaseException{
        if (bookmarkDao.checkUser(userIdx) == 0 ) {
            throw new BaseException(INVALID_USER);
        }
        if(bookmarkDao.checkProduct(productIdx) == 0) {
            throw new BaseException(INVALID_PRODUCT);
        }
        try {

            int result = bookmarkDao.existBookmark(userIdx,productIdx);

            return result;

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
