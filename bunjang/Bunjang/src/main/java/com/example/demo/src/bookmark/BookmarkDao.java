package com.example.demo.src.bookmark;

import com.example.demo.src.bookmark.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository

public class BookmarkDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 찜 추가
    public int createBookmark(int userIdx, int productIdx) {
        String createBookmarkQuery = "INSERT INTO bunjang.BookMarks (userId, productId ) VALUES (?, ?)";
        Object[] createBookmarkParams = new Object[]{userIdx,productIdx};
        return this.jdbcTemplate.update(createBookmarkQuery,createBookmarkParams);
    }
    // 찜 여부 확인
    public int checkBookmark(int userIdx, int productIdx) {
        String checkBookmarkQuery = "select exists(select *\n" +
                "from BookMarks\n" +
                "where BookMarks.userId = ? and BookMarks.productId = ?)";
        Object[] checkBookmarkParams = new Object[]{userIdx,productIdx};
        return this.jdbcTemplate.queryForObject(checkBookmarkQuery,
                int.class,
        checkBookmarkParams);
    }
    // 찜 취소
    public int patchBookmark(int userIdx, int productIdx) {
        String patchBookmarkQuery = "DELETE FROM bunjang.BookMarks WHERE userId = ? and productId = ?";
        Object[] patchBookmarkParams = new Object[]{userIdx,productIdx};
        return this.jdbcTemplate.update(patchBookmarkQuery,patchBookmarkParams);
    }

    // 찜 조회
    public List<GetUserBookmarksRes> getUserBookmarks(int userIdx) {
        String getUserBookmarksQuery = "select BookMarks.productId, safePay , url1, productName, format(price, '###,###') as price, storeProfileImgUrl, storeName,\n" +
                "       (select case when TIMESTAMPDIFF(MINUTE ,Products.createAt, NOW()) < 60\n" +
                "then concat(TIMESTAMPDIFF(MINUTE ,Products.createAt, NOW()),'분 전')\n" +
                "    when TIMESTAMPDIFF(HOUR ,Products.createAt, NOW()) < 24\n" +
                "    then concat(TIMESTAMPDIFF(HOUR ,Products.createAt, NOW()), '시간 전')\n" +
                "    WHEN TIMESTAMPDIFF(DAY ,Products.createAt, NOW()) < 30\n" +
                "    then concat(TIMESTAMPDIFF(DAY ,Products.createAt, NOW()), '일 전')\n" +
                "end) as timeDiff\n" +
                "from Products\n" +
                "inner join BookMarks on BookMarks.productId = Products.id\n" +
                "inner join ProductImgUrls on ProductImgUrls.productId = Products.id\n" +
                "inner join Users on Users.userId = Products.userId\n" +
                "inner join Stores on Stores.userId = Users.userId\n" +
                "where BookMarks.userId = ?";
        int getUserBookmarksParams = userIdx;

        return this.jdbcTemplate.query(getUserBookmarksQuery,
                (rs, rowNum) -> new GetUserBookmarksRes(
                        rs.getInt("productId"),
                        rs.getString("safePay"),
                        rs.getString("url1"),
                        rs.getString("productName"),
                        rs.getString("price"),
                        rs.getString("storeProfileImgUrl"),
                        rs.getString("storeName"),
                        rs.getString("timeDiff")),
                getUserBookmarksParams);
    }

    // 유저가 상품을 찜했는지 여부
    public int existBookmark(int userIdx, int productIdx) {

        String existBookmarkQuery = "select exists(select *\n" +
                "from BookMarks\n" +
                "where userId = ? and productId = ?) as bookmarkExist";
        Object[] existBookmarkParams = new Object[]{userIdx,productIdx};

        return this.jdbcTemplate.queryForObject(existBookmarkQuery,
                int.class,
                existBookmarkParams);
    }

    // 유저 존재하는지 체크
    public int checkUser(int userIdx) {
        String checkUserQuery = "select exists(select userId\n" +
                "from Users\n" +
                "where userId = ?) checkUser";
        int checkUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserQuery,
                int.class,
                checkUserParams);
    }

    // 상품 존재하는지 체크
    public int checkProduct(int productIdx) {
        String checkProductQuery = "select exists(select Products.id\n" +
                "from Products\n" +
                "where Products.id = ?) checkProduct";
        int checkProductParams = productIdx;
        return this.jdbcTemplate.queryForObject(checkProductQuery,
                int.class,
                checkProductParams);
    }


}
