package com.example.demo.src.account;

import com.example.demo.src.account.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository

public class AccountDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    String a = "A";
    String d = "D";
    // 유저의 계좌 개수 조회
    public int createAccountCnt(int userIdx) {
        String createAccountCntQuery = "select count(Account.id)\n" +
                "from Account\n" +
                "where Account.usrId = ?";
        int createAccountCntParams = userIdx;
        return this.jdbcTemplate.queryForObject(createAccountCntQuery,
                int.class,
                createAccountCntParams);
    }

    //  // 계좌가 없는 경우 무조건 대표계좌  'A' 로 넣기
    public int creatUserAccountForA(int userIdx, PostUserAccountReq postUserAccountReq) {
        String creatUserAccountForAQuery = "INSERT INTO bunjang.Account (usrId, accountHolder, bankId, accountNum, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        Object[] creatUserAccountForAParams = new Object[]{userIdx,postUserAccountReq.getAccountHolder(),postUserAccountReq.getBankId(),
        postUserAccountReq.getAccountNum(),a};

        return this.jdbcTemplate.update(creatUserAccountForAQuery,creatUserAccountForAParams);
    }

    //계좌를 1개 등록한 경우 A로 값이 들어오면 A로 넣고 원래 있던 계좌는 D로 바꾸기
    public int createUserAccountModify(int userIdx, PostUserAccountReq postUserAccountReq) {
        // 유저의 첫번째 계좌 id 추출
        String userFirstAccountIdQuery = "select Account.id as accountId\n" +
                "from Account\n" +
                "where Account.usrId = ?";
        int userFirstAccountIdParams = userIdx;

        int userFirstAccountId = this.jdbcTemplate.queryForObject(userFirstAccountIdQuery,
                int.class,
                userFirstAccountIdParams);

        //첫번째 계좌 D로 바꾸기
        String modifyForDQuery = "UPDATE bunjang.Account t SET t.status = 'D' WHERE t.id = ?";
        int modifyForDParams = userFirstAccountId;
        this.jdbcTemplate.update(modifyForDQuery,modifyForDParams);

        // 두번째 계좌 A로 등록
        String createUserAccountModifyQuery = "INSERT INTO bunjang.Account (usrId, accountHolder, bankId, accountNum, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        Object[] createUserAccountModifyParams = new Object[]{userIdx,postUserAccountReq.getAccountHolder(),postUserAccountReq.getBankId(),
                postUserAccountReq.getAccountNum(),a};

        return this.jdbcTemplate.update(createUserAccountModifyQuery,createUserAccountModifyParams);
    }

    // 계좌가 1개있고 status가 D인 경우 그냥 D 넣기
    public int createUserAccountForD(int userIdx, PostUserAccountReq postUserAccountReq) {
        String createUserAccountForDQuery = "INSERT INTO bunjang.Account (usrId, accountHolder, bankId, accountNum, status) " +
                "VALUES (?, ?, ?, ?, ?)";
        Object[] createUserAccountForDParams = new Object[]{userIdx,postUserAccountReq.getAccountHolder(),postUserAccountReq.getBankId(),
                postUserAccountReq.getAccountNum(),d};

        return this.jdbcTemplate.update(createUserAccountForDQuery,createUserAccountForDParams);
    }

    // 계좌 1개만 있을때 삭제
    public int deleteUserAccountFor1(int accountIdx) {
        String deleteUserAccountFor1Query = "DELETE FROM bunjang.Account WHERE id = ?";
        int deleteUserAccountFor1Params = accountIdx;

        return this.jdbcTemplate.update(deleteUserAccountFor1Query,deleteUserAccountFor1Params);
    }

    // 계좌 2개 있을때 한개 삭제하고 나머지 1개 A로 바꾸기
    public int deleteUserAccountFor2(int userIdx, int accountIdx) {

        String deleteUserAccountFor2Query = "DELETE FROM bunjang.Account WHERE id = ?";
        int deleteUserAccountFor2Params = accountIdx;

        this.jdbcTemplate.update(deleteUserAccountFor2Query,deleteUserAccountFor2Params);

        // 유저의 나머지 계좌 id 추출
        String userFirstAccountIdQuery = "select Account.id as accountId\n" +
                "from Account\n" +
                "where Account.usrId = ?";
        int userFirstAccountIdParams = userIdx;

        int userFirstAccountId = this.jdbcTemplate.queryForObject(userFirstAccountIdQuery,
                int.class,
                userFirstAccountIdParams);

        //첫번째 계좌 A로 바꾸기
        String modifyForDQuery = "UPDATE bunjang.Account t SET t.status = 'A' WHERE t.id = ?";
        int modifyForDParams = userFirstAccountId;
        return this.jdbcTemplate.update(modifyForDQuery,modifyForDParams);
    }

    // 계좌 조회
   public List<GerUserAccountRes> getUserAccount(int userIdx) {
        String getUserAccountQuery = "select case when Account.status = 'A' then '기본계좌'\n" +
                "    when Account.status = 'D' then '일반계좌'\n" +
                "end as bankStatus, bankName, accountNum, accountHolder\n" +
                "from Account\n" +
                "inner join Bank on Bank.id = Account.bankId\n" +
                "where usrId = ?\n" +
                "order by Account.status";

        int getUserAccountParams = userIdx;

        return this.jdbcTemplate.query(getUserAccountQuery,
                (rs, rowNum) -> new GerUserAccountRes(
                        rs.getString("bankStatus"),
                        rs.getString("bankName"),
                        rs.getString("accountNum"),
                        rs.getString("accountHolder")),
                getUserAccountParams);
   }

}
