package com.example.demo.src.category;

import com.example.demo.src.category.model.*;
import com.example.demo.src.product.model.GetMainProductsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository

public class CategoryDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 첫번째 카테고리 상품 조회
    public List<GetMainProductsRes> getFirstCategoryProducts(int firstIdx) {

        String getFirstCategoryProductsQuery = "select Products.id, url1, format(price, '###,###') as price, productName, location,\n" +
                "       (select case when TIMESTAMPDIFF(MINUTE ,Products.createAt, NOW()) < 60\n" +
                "then concat(TIMESTAMPDIFF(MINUTE ,Products.createAt, NOW()),'분 전')\n" +
                "    when TIMESTAMPDIFF(HOUR ,Products.createAt, NOW()) < 24\n" +
                "    then concat(TIMESTAMPDIFF(HOUR ,Products.createAt, NOW()), '시간 전')\n" +
                "    WHEN TIMESTAMPDIFF(DAY ,Products.createAt, NOW()) < 30\n" +
                "    then concat(TIMESTAMPDIFF(DAY ,Products.createAt, NOW()), '일 전')\n" +
                "end) as timeDiff , safePay, count( BookMarks.productId) as bookmarkCnt\n" +
                "from Products\n" +
                "inner join FirstCategory on FirstCategory.categoryName = Products.firstCategoryName\n" +
                "inner join ProductImgUrls on ProductImgUrls.productId = Products.id\n" +
                "left join BookMarks on BookMarks.productId = Products.id\n" +
                "where FirstCategory.firstCategoryId = ?\n" +
                "group by Products.id";
        int getFirstCategoryProductsParams = firstIdx;

        return this.jdbcTemplate.query(getFirstCategoryProductsQuery,
                (rs, rowNum) -> new GetMainProductsRes(
                        rs.getInt("id"),
                        rs.getString("url1"),
                        rs.getString("price"),
                        rs.getString("productName"),
                        rs.getString("location"),
                        rs.getString("timeDiff"),
                        rs.getString("safePay"),
                        rs.getInt("bookmarkCnt")),
                getFirstCategoryProductsParams);
    }

    // 두번째 카테고리 상품 조회
    public List<GetMainProductsRes> getSecondCategoryProducts(int firstIdx,int secondIdx) {

        String getSecondCategoryProductsQuery = "select Products.id, url1, format(price, '###,###') as price, productName, location,\n" +
                "       (select case when TIMESTAMPDIFF(MINUTE ,Products.createAt, NOW()) < 60\n" +
                "then concat(TIMESTAMPDIFF(MINUTE ,Products.createAt, NOW()),'분 전')\n" +
                "    when TIMESTAMPDIFF(HOUR ,Products.createAt, NOW()) < 24\n" +
                "    then concat(TIMESTAMPDIFF(HOUR ,Products.createAt, NOW()), '시간 전')\n" +
                "    WHEN TIMESTAMPDIFF(DAY ,Products.createAt, NOW()) < 30\n" +
                "    then concat(TIMESTAMPDIFF(DAY ,Products.createAt, NOW()), '일 전')\n" +
                "end) as timeDiff , safePay, count( BookMarks.productId) as bookmarkCnt\n" +
                "from Products\n" +
                "inner join FirstCategory on FirstCategory.categoryName = Products.firstCategoryName\n" +
                "inner join SecondCategory on SecondCategory.categoryName = Products.secondCategoryName\n" +
                "inner join ProductImgUrls on ProductImgUrls.productId = Products.id\n" +
                "left join BookMarks on BookMarks.productId = Products.id\n" +
                "where FirstCategory.firstCategoryId = ? and SecondCategory.secondCategoryId = ?\n" +
                "group by Products.id;";

        Object[] getSecondCategoryProductsParams = new Object[]{firstIdx,secondIdx};

        return this.jdbcTemplate.query(getSecondCategoryProductsQuery,
                (rs, rowNum) -> new GetMainProductsRes(
                        rs.getInt("id"),
                        rs.getString("url1"),
                        rs.getString("price"),
                        rs.getString("productName"),
                        rs.getString("location"),
                        rs.getString("timeDiff"),
                        rs.getString("safePay"),
                        rs.getInt("bookmarkCnt")),
                getSecondCategoryProductsParams);

    }

    // 세번째 카테고리 상품 조회
    public List<GetMainProductsRes> getThirdCategoryProducts(int firstIdx,int secondIdx, int thirdIdx) {

        String getThirdCategoryProductsQuery = "select Products.id, url1, format(price, '###,###') as price, productName, location,\n" +
                "       (select case when TIMESTAMPDIFF(MINUTE ,Products.createAt, NOW()) < 60\n" +
                "then concat(TIMESTAMPDIFF(MINUTE ,Products.createAt, NOW()),'분 전')\n" +
                "    when TIMESTAMPDIFF(HOUR ,Products.createAt, NOW()) < 24\n" +
                "    then concat(TIMESTAMPDIFF(HOUR ,Products.createAt, NOW()), '시간 전')\n" +
                "    WHEN TIMESTAMPDIFF(DAY ,Products.createAt, NOW()) < 30\n" +
                "    then concat(TIMESTAMPDIFF(DAY ,Products.createAt, NOW()), '일 전')\n" +
                "end) as timeDiff , safePay, count( BookMarks.productId) as bookmarkCnt\n" +
                "from Products\n" +
                "inner join FirstCategory on FirstCategory.categoryName = Products.firstCategoryName\n" +
                "inner join SecondCategory on SecondCategory.categoryName = Products.secondCategoryName\n" +
                "inner join ThirdCategory on ThirdCategory.categoryName = Products.thirdCategoryName\n" +
                "inner join ProductImgUrls on ProductImgUrls.productId = Products.id\n" +
                "left join BookMarks on BookMarks.productId = Products.id\n" +
                "where FirstCategory.firstCategoryId = ? and SecondCategory.secondCategoryId = ? and ThirdCategory.thirdCategoryId = ?\n" +
                "group by Products.id;";

        Object[] getThirdCategoryProductsParams = new Object[]{firstIdx,secondIdx,thirdIdx};

        return this.jdbcTemplate.query(getThirdCategoryProductsQuery,
                (rs, rowNum) -> new GetMainProductsRes(
                        rs.getInt("id"),
                        rs.getString("url1"),
                        rs.getString("price"),
                        rs.getString("productName"),
                        rs.getString("location"),
                        rs.getString("timeDiff"),
                        rs.getString("safePay"),
                        rs.getInt("bookmarkCnt")),
                getThirdCategoryProductsParams);
    }


}
