package com.example.demo.src.product;

import com.example.demo.src.product.model.*;
import com.example.demo.src.product.model.GetKeywordRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository

public class ProductDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    // 상품 상세 페이지 조회
    public GetProductDetailRes getProductDetail(int productIdx,int userIdx) {

        // 회원이 상품 조회했을때 기록 남기는 용도
        String userViewProductQuery = "INSERT INTO bunjang.UserViewProduct (userId, productId) VALUES (?, ?)";
        Object[] userViewProductParams = new Object[]{userIdx,productIdx};
        this.jdbcTemplate.update(userViewProductQuery,userViewProductParams);

        // 상세 페이지 조회
        String getProductDetailQuery = "select Products.id, format(price, '###,###') as price, safePay, productName, location,\n" +
                "       (select case when TIMESTAMPDIFF(MINUTE ,Products.createAt, NOW()) < 60\n" +
                "then concat(TIMESTAMPDIFF(MINUTE ,Products.createAt, NOW()),'분 전')\n" +
                "    when TIMESTAMPDIFF(HOUR ,Products.createAt, NOW()) < 24\n" +
                "    then concat(TIMESTAMPDIFF(HOUR ,Products.createAt, NOW()), '시간 전')\n" +
                "    WHEN TIMESTAMPDIFF(DAY ,Products.createAt, NOW()) < 30\n" +
                "    then concat(TIMESTAMPDIFF(DAY ,Products.createAt, NOW()), '일 전')\n" +
                "end) as timeDiff, productStatus, count, deliveryTip, trade, description,\n" +
                "                  (select case when Products.thirdCategoryName is null and Products.secondCategoryName is null\n" +
                "then Products.firstCategoryName\n" +
                "when Products.thirdCategoryName is null and Products.secondCategoryName is not null\n" +
                "then Products.secondCategoryName\n" +
                "    when Products.thirdCategoryName is not null\n" +
                "    then Products.thirdCategoryName\n" +
                "                   end) as category, count( BookMarks.productId) as bookmarkCnt\n" +
                "from Products\n" +
                "left join BookMarks on BookMarks.productId = Products.id\n" +
                "group by Products.id\n" +
                "having Products.id = ?";
        int getProductDetailParams = productIdx;

        String getProductImgQuery = "select url1, url2, url3,url4,url5,url6,url7,url8,url9,url10,url11,url12\n" +
                "from ProductImgUrls\n" +
                "inner join Products on Products.id = ProductImgUrls.productId\n" +
                "where Products.id = ?";
        int getProductImgParams = productIdx;
        GetProductImgRes getProductImgRes = this.jdbcTemplate.queryForObject(getProductImgQuery,
                (rs, rowNum) -> new GetProductImgRes(
                        rs.getString("url1"),
                        rs.getString("url2"),
                        rs.getString("url3"),
                        rs.getString("url4"),
                        rs.getString("url5"),
                        rs.getString("url6"),
                        rs.getString("url7"),
                        rs.getString("url8"),
                        rs.getString("url9"),
                        rs.getString("url10"),
                        rs.getString("url11"),
                        rs.getString("url12")
                ),
                getProductImgParams);

        String getProductTagQuery = "select tag1, tag2, tag3, tag4, tag5\n" +
                "from Products\n" +
                "inner join ProductTags on ProductTags.productId = Products.id\n" +
                "where Products.id = ?";
        int getProductTagParams = productIdx;
        GetProductTagRes getProductTagRes = this.jdbcTemplate.queryForObject(getProductTagQuery,
                (rs, rowNum) -> new GetProductTagRes(
                        rs.getString("tag1"),
                        rs.getString("tag2"),
                        rs.getString("tag3"),
                        rs.getString("tag4"),
                        rs.getString("tag5")
                ),
                getProductTagParams);

        return this.jdbcTemplate.queryForObject(getProductDetailQuery,
                (rs, rowNum) -> new GetProductDetailRes(
                        getProductImgRes,
                        rs.getInt("id"),
                        rs.getString("price"),
                        rs.getString("safePay"),
                        rs.getString("productName"),
                        rs.getString("location"),
                        rs.getString("timeDiff"),
                        rs.getString("productStatus"),
                        rs.getInt("count"),
                        rs.getString("deliveryTip"),
                        rs.getString("trade"),
                        rs.getString("description"),
                        rs.getString("category"),
                        rs.getInt("bookmarkCnt"),
                        getProductTagRes
                ),
                getProductDetailParams);
    }

    // 메인 화면 추천 상품 조회
    public List<GetMainProductsRes> getMainProducts(){
        String getMainProductsQuery = "select Products.id, url1, format(price, '###,###') as price, productName, location,\n" +
                "       (select case when TIMESTAMPDIFF(MINUTE ,Products.createAt, NOW()) < 60\n" +
                "then concat(TIMESTAMPDIFF(MINUTE ,Products.createAt, NOW()),'분 전')\n" +
                "    when TIMESTAMPDIFF(HOUR ,Products.createAt, NOW()) < 24\n" +
                "    then concat(TIMESTAMPDIFF(HOUR ,Products.createAt, NOW()), '시간 전')\n" +
                "    WHEN TIMESTAMPDIFF(DAY ,Products.createAt, NOW()) < 30\n" +
                "    then concat(TIMESTAMPDIFF(DAY ,Products.createAt, NOW()), '일 전')\n" +
                "end) as timeDiff , safePay, count( BookMarks.productId) as bookmarkCnt\n" +
                "from Products\n" +
                "inner join ProductImgUrls on ProductImgUrls.productId = Products.id\n" +
                "left join BookMarks on BookMarks.productId = Products.id\n" +
                "group by Products.id";

        return this.jdbcTemplate.query(getMainProductsQuery,
                (rs, rowNum) -> new GetMainProductsRes(
                        rs.getInt("id"),
                        rs.getString("url1"),
                        rs.getString("price"),
                        rs.getString("productName"),
                        rs.getString("location"),
                        rs.getString("timeDiff"),
                        rs.getString("safePay"),
                        rs.getInt("bookmarkCnt"))
        );
    }


    // 상품 등록 태그 조회
    public List<GetTagsRes> getTags(String tagWord) {
        String getTagsQuery = "select word\n" +
                "from Words\n" +
                "where word like concat('%',?,'%')";
        String getTagsParams = tagWord;

        return this.jdbcTemplate.query(getTagsQuery,
                (rs, rowNum) -> new GetTagsRes(
                        rs.getString("word")),
                getTagsParams);
    }

    // 상품 등록 첫번째 카테고리 조회
    public List<GetFirstCategoryRes> getFirstCategory() {
        String getFirstCategoryQuery = "select categoryName\n" +
                "from FirstCategory";

        return this.jdbcTemplate.query(getFirstCategoryQuery,
                (rs, rowNum) -> new GetFirstCategoryRes(
                        rs.getString("categoryName"))
                );
    }
    // 상품 등록 두번째 카테고리 조회
    public List<GetSecondCategoryRes> getSecondCategory(int firstIdx) {
        String getSecondCategoryQuery = "select SecondCategory.categoryName, SecondCategory.secondCategoryId , thirdCategoryId\n" +
                "from SecondCategory\n" +
                "inner join FirstCategory on FirstCategory.firstCategoryId = SecondCategory.firstCategoryId\n" +
                "left join ThirdCategory on ThirdCategory.secondCategoryId = SecondCategory.secondCategoryId\n" +
                "where FirstCategory.firstCategoryId = ?\n" +
                "group by SecondCategory.secondCategoryId";
        int getSecondCategoryParams = firstIdx;

        return this.jdbcTemplate.query(getSecondCategoryQuery,
                (rs, rowNum) -> new GetSecondCategoryRes(
                        rs.getString("categoryName"),
                        rs.getInt("secondCategoryId"),
                        rs.getInt("thirdCategoryId")),
                getSecondCategoryParams);
    }
    // 상품 등록 세번째 카테고리 조회
    public List<GetThirdCategoryRes> getThirdCategory(int firstIdx, int secondIdx) {
        String getThirdCategoryQuery = "select ThirdCategory.categoryName, ThirdCategory.thirdCategoryId \n" +
                "from ThirdCategory\n" +
                "inner join SecondCategory on SecondCategory.secondCategoryId = ThirdCategory.secondCategoryId\n" +
                "inner join FirstCategory on FirstCategory.firstCategoryId = SecondCategory.firstCategoryId\n" +
                "where FirstCategory.firstCategoryId = ? and SecondCategory.secondCategoryId = ?";
        Object[] getThirdCategoryParams = new Object[]{firstIdx,secondIdx};

        return this.jdbcTemplate.query(getThirdCategoryQuery,
                (rs, rowNum) -> new GetThirdCategoryRes(
                        rs.getString("categoryName"),
                        rs.getInt("thirdCategoryId")),
                getThirdCategoryParams);
    }

    // 상품 등록
    public int postProduct(int userIdx, PostProductReq postProductReq) {

        // 상품 정보 입력
        String postProductReqQuery = "INSERT INTO bunjang.Products (userId, productName, firstCategoryName, secondCategoryName, thirdCategoryName,\n" +
                "                                 price, deliveryTip, count, productStatus, trade, location, description, safePay)\n" +
                "VALUES (?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] postProductReqParams = new Object[]{userIdx,postProductReq.getProductName(),postProductReq.getFirstCategoryName(),postProductReq.getSecondCategoryName(),
        postProductReq.getThirdCategoryName(),postProductReq.getPrice(),postProductReq.getDeliveryTip(),postProductReq.getCount(),
        postProductReq.getProductStatus(),postProductReq.getTrade(),postProductReq.getLocation(),
        postProductReq.getDescription(),postProductReq.getSafePay()};

        this.jdbcTemplate.update(postProductReqQuery,postProductReqParams);

        String lastInsertIdQuery = "select last_insert_id()";
        int productId = this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);

        // 상품 이미지 등록
        String postProductReqImgUrlsQuery = "INSERT INTO bunjang.ProductImgUrls (productId, url1, url2, url3, url4, url5, url6, url7, url8, url9, url10, url11, url12)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] postProductReqImgUrlsParams = new Object[]{productId, postProductReq.getUrl1(),postProductReq.getUrl2(),postProductReq.getUrl3(),
        postProductReq.getUrl4(),postProductReq.getUrl5(),postProductReq.getUrl6(),postProductReq.getUrl7(),
        postProductReq.getUrl8(),postProductReq.getUrl9(),postProductReq.getUrl10(),postProductReq.getUrl11(),postProductReq.getUrl12()};

        this.jdbcTemplate.update(postProductReqImgUrlsQuery,postProductReqImgUrlsParams);

        // 상품 태그등록
        String postProductReqTagsQuery = "INSERT INTO bunjang.ProductTags (productId, tag1, tag2, tag3, tag4, tag5)\n" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        Object[] postProductReqTagsParams = new Object[]{productId,postProductReq.getTag1(),postProductReq.getTag2(),postProductReq.getTag3(),
        postProductReq.getTag4(),postProductReq.getTag5()};

        return this.jdbcTemplate.update(postProductReqTagsQuery,postProductReqTagsParams);

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

    //키워드로 검색
    public List<GetMainProductsRes> searchProducts(String keyword){
        String searchProductsQuery = "select Products.id, url1, format(price, '###,###') as price, productName, location,\n" +
                "                       (select case when TIMESTAMPDIFF(MINUTE ,Products.createAt, NOW()) < 60\n" +
                "                then concat(TIMESTAMPDIFF(MINUTE ,Products.createAt, NOW()),'분 전')\n" +
                "                    when TIMESTAMPDIFF(HOUR ,Products.createAt, NOW()) < 24\n" +
                "                    then concat(TIMESTAMPDIFF(HOUR ,Products.createAt, NOW()), '시간 전')\n" +
                "                    WHEN TIMESTAMPDIFF(DAY ,Products.createAt, NOW()) < 30\n" +
                "                    then concat(TIMESTAMPDIFF(DAY ,Products.createAt, NOW()), '일 전')\n" +
                "                end) as timeDiff , safePay, count( BookMarks.productId) as bookmarkCnt\n" +
                "                from Products\n" +
                "                inner join ProductImgUrls on ProductImgUrls.productId = Products.id\n" +
                "                left join BookMarks on BookMarks.productId = Products.id\n" +
                "                where productName like concat('%',?,'%') group by productName";

        return this.jdbcTemplate.query(searchProductsQuery,
                (rs, rowNum) -> new GetMainProductsRes(
                        rs.getInt("id"),
                        rs.getString("url1"),
                        rs.getString("price"),
                        rs.getString("productName"),
                        rs.getString("location"),
                        rs.getString("timeDiff"),
                        rs.getString("safePay"),
                        rs.getInt("bookmarkCnt")),keyword
        );
    }

    public List<GetKeywordRes> getKeywords(String keyword){
        String getKeywordsQuery="select productName,id from Products where productName like concat( ?,'%')";
        return this.jdbcTemplate.query(getKeywordsQuery,
                (rs, rowNum) -> new GetKeywordRes(
                        rs.getString("productName"),
                        rs.getInt("id")),keyword
        );
    }

    public List<GetStoreKeywordRes> getStoreKeywords(String keyword){
        String getStoreKeywordQuery="select storeName, storeProfileImgUrl, Stores.storeId from\n" +
                "Stores where storeName like concat('%',?,'%')";
        String getStoreIdQuery="select storeId from Stores where storeName like concat('%',?,'%')";
        int userId=this.jdbcTemplate.queryForObject(getStoreIdQuery,int.class,keyword);
        String getFollowerQuery="select count(Followers.userId) from Followers where userId=?";
        String getProductCntQuery="select count(Products.userId) from Products where userId=?";
        return this.jdbcTemplate.query(getStoreKeywordQuery,
                (rs, rowNum) -> new GetStoreKeywordRes(
                        rs.getString("storeName"),
                        rs.getString("storeProfileImgUrl"),
                        rs.getInt("storeId"),
                        this.jdbcTemplate.queryForObject(getFollowerQuery,int.class,userId),
                        this.jdbcTemplate.queryForObject(getProductCntQuery,int.class,userId)
                        ),keyword
        );
    }




    
}


