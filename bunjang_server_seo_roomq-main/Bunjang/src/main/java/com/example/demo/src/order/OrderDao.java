package com.example.demo.src.order;

import com.example.demo.src.address.model.GetAddressRes;
import com.example.demo.src.order.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class OrderDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //전체 구매내역 조회
    public List<GetOrderRes> getOrders(int userId) {
        String getOrdersQuery = "select url1, productName, Orders.status, storeName,price,date_format(Orders.updateAt,'%Y.%m.%d (%r)')AS orderDate\n" +
                "from Products join ProductImgUrls on ProductImgUrls.productId=Products.id\n" +
                "join Stores S on Products.userId = S.userId\n" +
                "join Orders on Orders.productId=Products.id\n" +
                "where Orders.userId=?";
        return this.jdbcTemplate.query(getOrdersQuery,
                (rs, rowNum) -> new GetOrderRes(
                        rs.getString("url1"),
                        rs.getString("productName"),
                        rs.getString("status"),
                        rs.getString("storeName"),
                        rs.getInt("price"),
                        rs.getString("orderDate")
                ),
                userId);
    }

    //취소/환불 조회
    public List<GetOrderRes> getOrdersCancel(int userId){
        String getOrdersCancelQuery="select url1, productName, Orders.status, storeName,price, date_format(Orders.updateAt,'%Y.%m.%d (%r)')AS orderDate\n" +
                "from Products join ProductImgUrls on ProductImgUrls.productId=Products.id\n" +
                "join Stores S on Products.userId = S.userId\n" +
                "join Orders on Orders.productId=Products.id\n" +
                "where Orders.userId=? and Orders.status=? or Orders.status=? ";
        return this.jdbcTemplate.query(getOrdersCancelQuery,
                (rs, rowNum) -> new GetOrderRes(
                        rs.getString("url1"),
                        rs.getString("productName"),
                        rs.getString("status"),
                        rs.getString("storeName"),
                        rs.getInt("price"),
                        rs.getString("orderDate")
                ),
                userId,"환불완료","결제취소");
    }

    //진행중
    public List<GetOrderRes> getOrdersProgress(int userId){
        String getOrdersQuery="select url1, productName, Orders.status, storeName,price, date_format(Orders.createAt,'%Y.%m.%d (%r)') AS orderDate\n" +
                "from Products join ProductImgUrls on ProductImgUrls.productId=Products.id\n" +
                "join Stores S on Products.userId = S.userId\n" +
                "join Orders on Orders.productId=Products.id\n" +
                "where Orders.userId=? and Orders.status=?";
        return this.jdbcTemplate.query(getOrdersQuery,
                (rs, rowNum) -> new GetOrderRes(
                        rs.getString("url1"),
                        rs.getString("productName"),
                        rs.getString("status"),
                        rs.getString("storeName"),
                        rs.getInt("price"),
                        rs.getString("orderDate")
                ),
                userId,"진행중");
    }

    //완료
    public List<GetOrderRes> getOrdersComplete(int userId) {
        String getOrdersQuery = "select url1, productName, Orders.status, storeName,price, date_format(Orders.createAt,'%Y.%m.%d (%r)')AS orderDate\n" +
                "from Products join ProductImgUrls on ProductImgUrls.productId=Products.id\n" +
                "join Stores S on Products.userId = S.userId\n" +
                "join Orders on Orders.productId=Products.id\n" +
                "where Orders.userId=? and Orders.status=?";
        return this.jdbcTemplate.query(getOrdersQuery,
                (rs, rowNum) -> new GetOrderRes(
                        rs.getString("url1"),
                        rs.getString("productName"),
                        rs.getString("status"),
                        rs.getString("storeName"),
                        rs.getInt("price"),
                        rs.getString("orderDate")
                ),
                userId, "결제완료");
    }

    public GetOrderParcelRes getOrderParcel(int productId, int userId) {
        String getOrderParcelQuery = "select url1, productName, price from Products\n" +
                "join ProductImgUrls on ProductImgUrls.productId=Products.id\n" +
                "where Products.id = ?";
        String getAddressQuery = "select status, addressName, address, detailAddress, phone from DeliveryAddresses\n" +
                "where userId=? and status=?";
        GetAddressRes address = this.jdbcTemplate.queryForObject(getAddressQuery,
                (rs, rowNum) -> new GetAddressRes(
                        rs.getString("status"),
                        rs.getString("addressName"),
                        rs.getString("address"),
                        rs.getString("detailAddress"),
                        rs.getString("phone")
                ),
                userId, "기본");

        String getPointScoreQuery = "select pointScore from Points\n" +
                "where userId=?";
        int pointScore = this.jdbcTemplate.queryForObject(getPointScoreQuery, int.class, userId);
        String getPriceQuery = "select price from Products\n" +
                "where id=?";
        int fee = (int) (this.jdbcTemplate.queryForObject(getPriceQuery, int.class, productId) * 0.035);
        String getDeliveryTipQuery = "select deliveryTip from Products\n" +
                "where id=?";
        int deliveryFee = 0;
        if (this.jdbcTemplate.queryForObject(getDeliveryTipQuery, String.class, productId).equals("배송비별도"))
            deliveryFee = 3000;
        else deliveryFee = 0;
        int finalDeliveryFee = deliveryFee;
        return this.jdbcTemplate.queryForObject(getOrderParcelQuery,
                (rs, rowNum) -> new GetOrderParcelRes(
                        rs.getString("url1"),
                        rs.getString("productName"),
                        rs.getInt("price"),
                        address,
                        pointScore,
                        fee,
                        finalDeliveryFee
                ),
                productId);
    }

    public GetDirectOrderRes getDirectOrder(int productId, int userId) {
        String getDirectOrderQuery = "select url1, productName, price from Products\n" +
                "join ProductImgUrls on ProductImgUrls.productId=Products.id\n" +
                "where Products.id=?";
        String getPointScoreQuery = "select pointScore from Points\n" +
                "where userId=?";
        int pointScore = this.jdbcTemplate.queryForObject(getPointScoreQuery, int.class, userId);
        String getPriceQuery = "select price from Products\n" +
                "where id=?";
        int fee = (int) (this.jdbcTemplate.queryForObject(getPriceQuery, int.class, productId) * 0.035);
        return this.jdbcTemplate.queryForObject(getDirectOrderQuery,
                (rs, rowNum) -> new GetDirectOrderRes(
                        rs.getString("url1"),
                        rs.getString("productName"),
                        rs.getInt("price"),
                        pointScore,
                        fee
                ),
                productId);
    }

    public void postParcelOrder(PostOrderReq postOrderReq) {
        String postOrderQuery = "insert into Orders(userId,productId,pointScore,tradeMethod, totalPrice,paymentMethod, address, addressReq) values(?,?,?,?,?,?,?,?)";
        String getPriceQuery = "select price from Products\n" +
                "where id=?";
        int price = this.jdbcTemplate.queryForObject(getPriceQuery, int.class, postOrderReq.getProductId());
        int fee = (int) (price * 0.035);
        String getDeliveryTipQuery = "select deliveryTip from Products\n" +
                "where id=?";
        int deliveryFee = 0;
        if (this.jdbcTemplate.queryForObject(getDeliveryTipQuery, String.class, postOrderReq.getProductId()).equals("배송비별도"))
            deliveryFee = 3000;
        else deliveryFee = 0;
        int finalDeliveryFee = deliveryFee;
        int totalPrice = (price + fee + finalDeliveryFee) - postOrderReq.getPointScore();
        Object[] postOrderParams = new Object[]{postOrderReq.getUserId(), postOrderReq.getProductId(), postOrderReq.getPointScore(),
                "택배거래", totalPrice,postOrderReq.getPaymentMethod() ,postOrderReq.getAddress(), postOrderReq.getAddressReq()
        };
        String postSalesQuery = "insert into Sales(storeId,productId,pointScore,tradeMethod, totalPrice,paymentMethod, address, addressReq) values(?,?,?,?,?,?,?,?)";
        Object[] postSalesParams = new Object[]{postOrderReq.getUserId(), postOrderReq.getProductId(), postOrderReq.getPointScore(),
                "택배거래", totalPrice,postOrderReq.getPaymentMethod() ,postOrderReq.getAddress(), postOrderReq.getAddressReq()
        };
        this.jdbcTemplate.update(postSalesQuery,postSalesParams);
        this.jdbcTemplate.update(postOrderQuery, postOrderParams);
    }

    public void postDirectOrder(PostOrderReq postOrderReq) {
        String postOrderQuery = "insert into Orders(userId,productId,pointScore,tradeMethod, totalPrice, paymentMethod) values(?,?,?,?,?,?)";
        String getPriceQuery = "select price from Products\n" +
                "where id=?";
        int price = this.jdbcTemplate.queryForObject(getPriceQuery, int.class, postOrderReq.getProductId());
        int fee = (int) (price * 0.035);
        int totalPrice = (price + fee) - postOrderReq.getPointScore();
        Object[] postOrderParams = new Object[]{postOrderReq.getUserId(), postOrderReq.getProductId(), postOrderReq.getPointScore(),
                "직거래", totalPrice,postOrderReq.getPaymentMethod()
        };

        Object[] postSalesParams = new Object[]{postOrderReq.getUserId(), postOrderReq.getProductId(), postOrderReq.getPointScore(),
                "직거래", totalPrice,postOrderReq.getPaymentMethod()
        };
        String postSalesQuery = "insert into Sales(storeId,productId,pointScore,tradeMethod, totalPrice, paymentMethod) values(?,?,?,?,?,?)";
        this.jdbcTemplate.update(postSalesQuery,postSalesParams);
        this.jdbcTemplate.update(postOrderQuery, postOrderParams);
    }


    public boolean checkOrder(int productId, int userId) {
        String checkOrderQuery = "select exists(select * from Orders where productId = ? and userId=?)";
        return this.jdbcTemplate.queryForObject(checkOrderQuery, boolean.class, productId, userId);
    }

    public GetTradeInfoRes getTradeInfo(int productId, int userId) {
        String getProductQuery = "select url1, productName, price , storeName from Products\n" +
                "join ProductImgUrls on ProductImgUrls.productId=Products.id\n" +
                "join Stores S on Products.userId = S.userId\n" +
                "where Products.id=?";

        String getTradeQuery="select tradeMethod,orderId,date_format(Orders.updateAt,'%Y.%m.%d (%r)') AS orderDate, paymentMethod,pointScore,\n" +
                "       userName,phone, address, totalPrice, pointScore from Orders join Users on Users.userId=Orders.userId\n" +
                "where Orders.userId=? and Orders.productId=?";
        String getPriceQuery = "select price from Products\n" +
                "where id=?";
        int price = this.jdbcTemplate.queryForObject(getPriceQuery, int.class, productId);
        int fee = (int) (price * 0.035);

        String getDeliveryTipQuery = "select deliveryTip from Products\n" +
                "where id=?";
        String deliveryFee = "무료배송";
        if (this.jdbcTemplate.queryForObject(getDeliveryTipQuery, String.class, productId).equals("배송비별도"))
            deliveryFee = "3000";
        else deliveryFee =  "무료배송";
        String finalDeliveryFee = deliveryFee;
       GetProductInfoRes productInfo= this.jdbcTemplate.queryForObject(getProductQuery,
               (rs, rowNum) -> new GetProductInfoRes(
                       rs.getString("url1"),
                       rs.getString("productName"),
                       rs.getInt("price"),
                       rs.getString("storeName")
               ),
               productId);
        return this.jdbcTemplate.queryForObject(getTradeQuery,
                (rs, rowNum) -> new GetTradeInfoRes(
                        productInfo,
                        rs.getString("tradeMethod"),
                        rs.getInt("orderId"),
                        rs.getString("orderDate"),
                        rs.getString("paymentMethod"),
                        rs.getString("userName"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getInt("totalPrice"),
                        rs.getInt("pointScore"),
                        fee,
                        finalDeliveryFee
                ),
                userId, productId);

    }

    public GetDirectTradeInfoRes getDirectTradeInfo(int productId, int userId) {
        String getProductQuery = "select url1, productName, price , storeName from Products\n" +
                "join ProductImgUrls on ProductImgUrls.productId=Products.id\n" +
                "join Stores S on Products.userId = S.userId\n" +
                "where Products.id=?";

        String getTradeQuery="select tradeMethod,orderId,date_format(Orders.updateAt,'%Y.%m.%d (%r)') AS orderDate, paymentMethod,pointScore,\n" +
                "       userName,phone, totalPrice, pointScore from Orders join Users on Users.userId=Orders.userId\n" +
                "where Orders.userId=? and Orders.productId=?";
        String getPriceQuery = "select price from Products\n" +
                "where id=?";
        int price = this.jdbcTemplate.queryForObject(getPriceQuery, int.class, productId);
        int fee = (int) (price * 0.035);

        GetProductInfoRes productInfo= this.jdbcTemplate.queryForObject(getProductQuery,
                (rs, rowNum) -> new GetProductInfoRes(
                        rs.getString("url1"),
                        rs.getString("productName"),
                        rs.getInt("price"),
                        rs.getString("storeName")
                ),
                productId);
        return this.jdbcTemplate.queryForObject(getTradeQuery,
                (rs, rowNum) -> new GetDirectTradeInfoRes(
                        productInfo,
                        rs.getString("tradeMethod"),
                        rs.getInt("orderId"),
                        rs.getString("orderDate"),
                        rs.getString("paymentMethod"),
                        rs.getString("userName"),
                        rs.getString("phone"),
                        rs.getInt("totalPrice"),
                        rs.getInt("pointScore"),
                        fee
                ),
                userId, productId);
    }

    public String checkTradeMethod(int productId, int userId){
        String checkTradeMethodQuery="select tradeMethod from Orders where productId=? and userId=?";
        return this.jdbcTemplate.queryForObject(checkTradeMethodQuery,String.class,productId,userId);
    }

    public boolean checkUser(int userId){
        String checkQuery="select exists(select * from Users where userId=?)";
        return this.jdbcTemplate.queryForObject(checkQuery,boolean.class,userId);
    }

    public boolean checkProduct(int productId){
        String checkQuery="select exists(select * from Products where Id=?)";
        return this.jdbcTemplate.queryForObject(checkQuery,boolean.class,productId);
    }

}
