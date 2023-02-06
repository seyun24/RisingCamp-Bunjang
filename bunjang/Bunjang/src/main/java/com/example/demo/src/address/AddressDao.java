package com.example.demo.src.address;

import com.example.demo.src.address.model.*;
import com.example.demo.src.product.model.GetThirdCategoryRes;
import com.example.demo.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class AddressDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //배송조회
    public List<GetAddressRes> getAddresses(int userId){
        String getAddressQuery="select status, addressName, address, detailAddress, phone from DeliveryAddresses\n" +
                "where userId=?";
        return this.jdbcTemplate.query(getAddressQuery,
                (rs, rowNum) -> new GetAddressRes(
                        rs.getString("status"),
                        rs.getString("addressName"),
                        rs.getString("address"),
                        rs.getString("detailAddress"),
                        rs.getString("phone")
                ),
                userId);
    }
    //베송추가
    public void addAddress(PostAddressReq postAddressReq){
        String addAddressQuery="insert into DeliveryAddresses(addressName, phone, address, detailAddress, status, userId) values (?,?,?,?,?,?)";
        Object[] addAddressParams=new Object[]{postAddressReq.getAddressName(),postAddressReq.getPhone(),postAddressReq.getAddress(),
        postAddressReq.getDetailAddress(),postAddressReq.getStatus(), postAddressReq.getUserId()};
        this.jdbcTemplate.update(addAddressQuery,addAddressParams);
    }
    //배송삭제
    public void deleteAddress(DeleteAddressReq deleteAddressReq){
        String deleteAddressQuery="delete from DeliveryAddresses where userId=? and addressName=?";
        Object[] deleteAddressParams=new Object[]{deleteAddressReq.getUserId(),deleteAddressReq.getAddressName()};
        this.jdbcTemplate.update(deleteAddressQuery,deleteAddressParams);
    }

    //기본 배송지 선택했을때 기존 기본배송지 디폴트로 변경
    public void postAddressStatus(PostAddressReq postAddressReq){
        String updateAddressStatusQuery="update DeliveryAddresses set status=default where userId=?";
        this.jdbcTemplate.update(updateAddressStatusQuery,postAddressReq.getUserId());
    }
    public void updateAddressStatus(PatchAddressReq patchAddressReq){
        String updateAddressStatusQuery="update DeliveryAddresses set status=default where userId=?";
        this.jdbcTemplate.update(updateAddressStatusQuery,patchAddressReq.getUserId());
    }

    //특정 배송지 조회
    public GetAddressRes getAddress(int userId, String addressName){
        String getAddressQuery="select status, addressName, address, detailAddress, phone from DeliveryAddresses\n" +
                "where userId=? and addressName=?";
        return this.jdbcTemplate.queryForObject(getAddressQuery,
                (rs, rowNum) -> new GetAddressRes(
                        rs.getString("status"),
                        rs.getString("addressName"),
                        rs.getString("address"),
                        rs.getString("detailAddress"),
                        rs.getString("phone")
                ),
                userId, addressName);
    }
    //배송지 수정
    public void modifyAddress(PatchAddressReq patchAddressReq){
        String modifyAddressQuery="update DeliveryAddresses set addressName=?, phone=?, address=?, detailAddress=?, status=? " +
                "where userId=? and  addressName=?";
        Object[] modifyAddressParams=new Object[]{patchAddressReq.getAddressName(),patchAddressReq.getPhone(),patchAddressReq.getAddress()
        ,patchAddressReq.getDetailAddress(),patchAddressReq.getStatus(),patchAddressReq.getUserId(),patchAddressReq.getAddressName()
        };
        this.jdbcTemplate.update(modifyAddressQuery,modifyAddressParams);

    }

    //주소 선택 조회
    public List<GetSelectAddressRes> getSelectAddress(int userId){
        String getSelectAddressQuery="select address, detailAddress, addressName, phone from DeliveryAddresses\n" +
                "where userId=?";
        return this.jdbcTemplate.query(getSelectAddressQuery,
                (rs, rowNum) -> new GetSelectAddressRes(
                        rs.getString("address"),
                        rs.getString("detailAddress"),
                        rs.getString("addressName"),
                        rs.getString("phone")
                ),
                userId);
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
