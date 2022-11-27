package com.example.demo.src.store;

import com.example.demo.src.product.model.GetProductDetailRes;
import com.example.demo.src.store.model.GetCntRes;
import com.example.demo.src.store.model.PostFollowReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class StoreDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void follow(int storeId, int userId){
        String followQuery="insert into Followings(storeId, userId) VALUES (?,?)";
        this.jdbcTemplate.update(followQuery,storeId,userId);
        String followerQuery="insert into Followers(storeId, userId) VALUES (?,?)";
        this.jdbcTemplate.update(followerQuery,userId,storeId);
    }

    public void unfollow(int storeId, int userId){
        String followQuery="delete from Followings where storeId=? and userId=?";
        this.jdbcTemplate.update(followQuery,storeId,userId);
        String followerQuery="delete from Followers where storeId=? and userId=?";
        this.jdbcTemplate.update(followerQuery,userId,storeId);
    }

    public boolean getFollowStatus(int storeId, int userId){
        String getFollowStatus="select EXISTS(select * from Followings where storeId=? and userId=?)";
        return this.jdbcTemplate.queryForObject(getFollowStatus,
                boolean.class, storeId,userId);
    }

    public int getFollower(int userId){
        String getFollowerQuery="select count(Followers.userId) from Followers where userId=?";
        return this.jdbcTemplate.queryForObject(getFollowerQuery,
                int.class,userId);
    }

    public int getFollowing(int userId){
        String getFollowingQuery="select count(Followings.userId) from Followings where userId=?";
        return this.jdbcTemplate.queryForObject(getFollowingQuery,
                int.class,userId);
    }
}
