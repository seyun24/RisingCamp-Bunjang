package com.example.demo.src.sale;

import com.example.demo.src.order.model.GetOrderRes;
import com.example.demo.src.sale.model.GetSaleRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SaleDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetSaleRes> getSales(int userId){
        String getSalesQuery="select url1, productName, Sales.status, storeName,price, date_format(Sales.updateAt,'%Y.%m.%d (%r)')AS orderDate\n" +
                "from Products join ProductImgUrls on ProductImgUrls.productId=Products.id\n" +
                "join Sales on Sales.productId=Products.id\n" +
                "join Stores S on Sales.storeId=S.storeId\n" +
                "where Sales.userId=?";
        return this.jdbcTemplate.query(getSalesQuery,
                (rs, rowNum) -> new GetSaleRes(
                        rs.getString("url1"),
                        rs.getString("productName"),
                        rs.getString("status"),
                        rs.getString("storeName"),
                        rs.getString("price"),
                        rs.getString("orderDate")
                ),
                userId);
    }

    public List<GetSaleRes> getSalesCancel(int userId){
        String getSalesQuery="select url1, productName, Sales.status, storeName,price, date_format(Sales.updateAt,'%Y.%m.%d (%r)')AS orderDate\n" +
                "from Products join ProductImgUrls on ProductImgUrls.productId=Products.id\n" +
                "join Sales on Sales.productId=Products.id\n" +
                "join Stores S on Sales.storeId=S.storeId\n" +
                "where Sales.userId=? and Sales.status=? or Sales.status=?";
        return this.jdbcTemplate.query(getSalesQuery,
                (rs, rowNum) -> new GetSaleRes(
                        rs.getString("url1"),
                        rs.getString("productName"),
                        rs.getString("status"),
                        rs.getString("storeName"),
                        rs.getString("price"),
                        rs.getString("orderDate")
                ),
                userId,"환불완료","판매취소");
    }

    public List<GetSaleRes> getSalesProgress(int userId){
        String getSalesQuery="select url1, productName, Sales.status, storeName,price, date_format(Sales.createAt,'%Y.%m.%d (%r)')AS orderDate\n" +
                "from Products join ProductImgUrls on ProductImgUrls.productId=Products.id\n" +
                "join Sales on Sales.productId=Products.id\n" +
                "join Stores S on Sales.storeId=S.storeId\n" +
                "where Sales.userId=? and Sales.status=?";
        return this.jdbcTemplate.query(getSalesQuery,
                (rs, rowNum) -> new GetSaleRes(
                        rs.getString("url1"),
                        rs.getString("productName"),
                        rs.getString("status"),
                        rs.getString("storeName"),
                        rs.getString("price"),
                        rs.getString("orderDate")
                ),
                userId,"진행중");
    }

    public List<GetSaleRes> getSalesComplete(int userId){
        String getSalesQuery="select url1, productName, Sales.status, storeName,price, date_format(Sales.createAt,'%Y.%m.%d (%r)')AS orderDate\n" +
                "from Products join ProductImgUrls on ProductImgUrls.productId=Products.id\n" +
                "join Sales on Sales.productId=Products.id\n" +
                "join Stores S on Sales.storeId=S.storeId\n" +
                "where Sales.userId=? and Sales.status=?";
        return this.jdbcTemplate.query(getSalesQuery,
                (rs, rowNum) -> new GetSaleRes(
                        rs.getString("url1"),
                        rs.getString("productName"),
                        rs.getString("status"),
                        rs.getString("storeName"),
                        rs.getString("price"),
                        rs.getString("orderDate")
                ),
                userId,"판매완료");
    }

}
