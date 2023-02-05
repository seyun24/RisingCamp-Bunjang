package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository


public class UserDao {



    private JdbcTemplate jdbcTemplate;
    private UserDao th;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    // 회원가입


        public int createUser(PostLoginReq postLoginReq) {
        String createUserQuery = "insert into Users (userName, firstNumber, birthdate, newsAgency, phone) VALUES (?,?,?,?,?)"; // 실행될 동적 쿼리문
        Object[] createUserParams = new Object[]{postLoginReq.getUserName(),postLoginReq.getFirstNumber(),
                postLoginReq.getBirthdate(),postLoginReq.getNewsAgency(),postLoginReq.getPhone()}; // 동적 쿼리의 ?부분에 주입될 값
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select userId from Users where phone=?"; // 가장 마지막에 삽입된(생성된) id값은 가져온다.
        int userId =this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class, postLoginReq.getPhone()); // 해당 쿼리문의 결과 마지막으로 삽인된 유저의 userIdx번호를 반환한다.
        String createStoreQuery="insert into Stores (userId, storeName) VALUES (?,?)";
        Object[] createStoreParams= new Object[]{userId,postLoginReq.getStoreName()};
        this.jdbcTemplate.update(createStoreQuery,createStoreParams);
        return userId;
    }


    // 이메일 확인
    public int checkEmail(String email) {
        String checkEmailQuery = "select exists(select email from User where email = ?)"; // User Table에 해당 email 값을 갖는 유저 정보가 존재하는가?
        String checkEmailParams = email; // 해당(확인할) 이메일 값
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams); // checkEmailQuery, checkEmailParams를 통해 가져온 값(intgud)을 반환한다. -> 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
    }




    public User getUserId(PostLoginReq postLoginReq) {
        String getQuery = "select * from Users where phone = ?";
        String getParams = postLoginReq.getPhone();

        return this.jdbcTemplate.queryForObject(getQuery,
                (rs, rowNum) -> new User(
                        rs.getInt("userId"),
                        rs.getString("userName"),
                        rs.getString("firstNumber"),
                        rs.getString("birthdate"),
                        rs.getString("newsAgency"),
                        rs.getString("phone")
                ),
                getParams
        );
    }



    // User 테이블에 존재하는 전체 유저들의 정보 조회
    public List<GetUserRes> getUsers() {
        String getUsersQuery = "select * from User";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("Email"),
                        rs.getString("password"))
        );
    }

    // 해당 nickname을 갖는 유저들의 정보 조회
    public List<GetUserRes> getUsersByNickname(String nickname) {
        String getUsersByNicknameQuery = "select * from User where nickname =?";
        String getUsersByNicknameParams = nickname;
        return this.jdbcTemplate.query(getUsersByNicknameQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUsersByNicknameParams);
    }

    // 해당 userIdx를 갖는 유저조회
    public GetUserRes getUser(int userIdx) {
        String getUserQuery = "select * from User where userIdx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUserParams);
    }


    public void createAuth(PostAuthNumReq postAuthNumReq){
            String createAuthQuery="insert into AuthNumbers(phone, number) values (?,?)";
            Object[] createAuthParams=new Object[]{postAuthNumReq.getPhone(),postAuthNumReq.getNumber()};
            this.jdbcTemplate.update(createAuthQuery,createAuthParams);
    }

    public void deleteAuth(PostAuthNumReq postAuthNumReq){
        String deleteAuthQuery="delete from AuthNumbers where phone=?";
        Object[] deleteAuthParams=new Object[]{postAuthNumReq.getPhone()};

            this.jdbcTemplate.update(deleteAuthQuery,deleteAuthParams);
    }


    public int checkNum(String phone, String number) {
        String checkAuthQuery = "select exists(select phone, number from AuthNumbers where phone=? and number=?)";
        Object[] checkAuthParams = new Object[]{phone, number};
        return this.jdbcTemplate.queryForObject(checkAuthQuery,
                int.class,
                checkAuthParams);
    }

    public int checkAccount(String phone) {
        String checkAccountQuery = "select exists(select phone from Users where phone=?)"; // User Table에 해당 email 값을 갖는 유저 정보가 존재하는가?
        return this.jdbcTemplate.queryForObject(checkAccountQuery,
                int.class,
               phone);
    }

    // 회원 상점 정보 수정
    public int modifyUserStoreInfo(int userIdx, PatchUserStoreInfoReq patchUserStoreInfoReq) {

            //유저 id의 상점 id 추출
        String getUserStoreIdQuery = "select storeId\n" +
                "from Stores\n" +
                "inner join Users on Stores.userId = Users.userId\n" +
                "where Users.userId = ?";
        int getUserStoreIdParams = userIdx;
        int storeIdx = this.jdbcTemplate.queryForObject(getUserStoreIdQuery,
                int.class,
                getUserStoreIdParams);

        String modifyUserStoreInfoQuery = "UPDATE bunjang.Stores t SET t.storeProfileImgUrl = ?, t.contactTimeStart = ?, t.contactTimeFinish = ?,\n" +
                "                            t.storeAdress = ?, t.storeName = ?, t.storeIntoro = ?,\n" +
                "                            t.notice = ?, t.info = ? WHERE t.storeId = ?";
        Object[] modifyUserStoreInfoParams = new Object[]{patchUserStoreInfoReq.getStoreProfileImgUrl(),patchUserStoreInfoReq.getContactTimeStart(),
        patchUserStoreInfoReq.getContactTimeFinish(),patchUserStoreInfoReq.getStoreAddress(),patchUserStoreInfoReq.getStoreName(),patchUserStoreInfoReq.getStoreInfo(),
        patchUserStoreInfoReq.getNotice(),patchUserStoreInfoReq.getInfo(), storeIdx};

        return this.jdbcTemplate.update(modifyUserStoreInfoQuery,modifyUserStoreInfoParams);
    }

    // 회원정보 변경
    public int modifyUserInfo(int userIdx, PatchUserInfoReq patchUserInfoReq) {

            String modifyUserInfoQuery = "UPDATE bunjang.Users t SET t.gender = ?, t.phone = ?, t.birthForSettings = ? WHERE t.userId = ?";

            Object[] modifyUserInfoParams = new Object[]{patchUserInfoReq.getGender(), patchUserInfoReq.getPhone(), patchUserInfoReq.getBirthForSettings(),userIdx};

            return this.jdbcTemplate.update(modifyUserInfoQuery,modifyUserInfoParams);

    }

    // 상점 차단
    public int createBlockStore(int userIdx, int storeIdx) {

            String createBlockStoreQuery = "INSERT INTO bunjang.BlockStores (userId, storeId) VALUES (? , ? )";

            Object[] createBlockStoreParams = new Object[]{userIdx,storeIdx};

            return this.jdbcTemplate.update(createBlockStoreQuery,createBlockStoreParams);
    }

    // 차단 상점 조회
    public List<GetUserBlockStoresRes> getUserBlockStores(int userIdx) {

            String getUserBlockStoresQuery = "select BlockStores.storeId, storeProfileImgUrl ,Stores.storeName,\n" +
                    "       (select case when TIMESTAMPDIFF(HOUR,BlockStores.createAt, NOW()) < 24 and TIMESTAMPDIFF(HOUR,BlockStores.createAt, NOW()) >= 0 and date_format(BlockStores.createAt, '%p') = 'PM'\n" +
                    "        then replace(date_format(BlockStores.createAt, '%p %h:%i'),'PM','오후')\n" +
                    "        when TIMESTAMPDIFF(HOUR,BlockStores.createAt, NOW()) < 24 and TIMESTAMPDIFF(HOUR,BlockStores.createAt, NOW()) > 0 and date_format(BlockStores.createAt, '%p') = 'AM'\n" +
                    "        then replace(date_format(BlockStores.createAt, '%p %h:%i'),'AM','오전')\n" +
                    "        when date_format(BlockStores.createAt, '%p') = 'PM'\n" +
                    "        then replace(date_format(BlockStores.createAt, '%y년 %m월 %d일 %p %h:%i'),'PM','오후')\n" +
                    "        when date_format(BlockStores.createAt, '%p') = 'AM'\n" +
                    "        then replace(date_format(BlockStores.createAt, '%y년 %m월 %d일 %p %h:%i'),'AM','오전')\n" +
                    "        end) as blockTime\n" +
                    "from BlockStores\n" +
                    "inner join Stores on Stores.storeId = BlockStores.storeId\n" +
                    "where BlockStores.userId = ?";
            int getUserBlockStoresParams = userIdx;

            return this.jdbcTemplate.query(getUserBlockStoresQuery,
                    (rs, rowNum) -> new GetUserBlockStoresRes(
                            rs.getInt("storeId"),
                            rs.getString("storeProfileImgUrl"),
                            rs.getString("storeName"),
                            rs.getString("blockTime")),
                    getUserBlockStoresParams);

    }

    // 최근 본 상품 조회
    public List<GetUserHistoryProductRes> getUserHistoryProduct(int userIdx) {
            String getUserHistoryProductQuery = "select distinct Products.id ,url1, productName, format(price, '###,###') as price, safePay\n" +
                    "from Products\n" +
                    "left join UserViewProduct on UserViewProduct.productId = Products.id\n" +
                    "inner join ProductImgUrls on ProductImgUrls.productId = Products.id\n" +
                    "where UserViewProduct.userId = ?\n" +
                    "order by UserViewProduct.createAt desc";
            int getUserHistoryProductParams = userIdx;

            return this.jdbcTemplate.query(getUserHistoryProductQuery,
                    (rs, rowNrm) -> new GetUserHistoryProductRes(
                            rs.getInt("id"),
                            rs.getString("url1"),
                            rs.getString("productName"),
                            rs.getString("price"),
                            rs.getString("safePay")),
                    getUserHistoryProductParams);

    }

    // 마이 페이지 조회
    public GetUserMyRes getUserMy(int userIdx) {
            String getUserMyQuery = "select storeProfileImgUrl, storeName,\n" +
                    "       (select count(bookMarkid)\n" +
                    "from BookMarks\n" +
                    "where BookMarks.userId = ?) bookmarkCnt\n" +
                    "from Users\n" +
                    "inner join Stores on Stores.userId = Users.userId\n" +
                    "where Users.userId = ?";
            Object[] getUserMyParams = new Object[]{userIdx,userIdx};

            String getUserMyProductsQuery = "select Products.id, url1, productName, format(price, '###,###') as price,\n" +
                    "       (select case when TIMESTAMPDIFF(SECOND ,Products.createAt,NOW()) <= 60\n" +
                    "       then concat(TIMESTAMPDIFF(SECOND ,Products.createAt,NOW()),'초 전')\n" +
                    "           when TIMESTAMPDIFF(MINUTE ,Products.createAt, NOW()) < 60\n" +
                    "then concat(TIMESTAMPDIFF(MINUTE ,Products.createAt, NOW()),'분 전')\n" +
                    "    when TIMESTAMPDIFF(HOUR ,Products.createAt, NOW()) < 24\n" +
                    "    then concat(TIMESTAMPDIFF(HOUR ,Products.createAt, NOW()), '시간 전')\n" +
                    "    WHEN TIMESTAMPDIFF(DAY ,Products.createAt, NOW()) < 30\n" +
                    "    then concat(TIMESTAMPDIFF(DAY ,Products.createAt, NOW()), '일 전')\n" +
                    "end) as timeDiff\n" +
                    "from Products\n" +
                    "inner join Users on Users.userId = Products.userId\n" +
                    "inner join Stores on Stores.userId = Users.userId\n" +
                    "inner join ProductImgUrls on ProductImgUrls.productId = Products.id\n" +
                    "where Products.userId = ?";
            int getUserMyProductsParams = userIdx;

            List<GetUserMyProductsRes> getUserMyProductsRes = this.jdbcTemplate.query(getUserMyProductsQuery,
                    (rs, rowNum) -> new GetUserMyProductsRes(
                            rs.getInt("id"),
                            rs.getString("url1"),
                            rs.getString("productName"),
                            rs.getString("price"),
                            rs.getString("timeDiff")),
                    getUserMyProductsParams);

        return this.jdbcTemplate.queryForObject(getUserMyQuery,
                (rs, rowNum) -> new GetUserMyRes(
                        rs.getString("storeProfileImgUrl"),
                        rs.getString("storeName"),
                        rs.getInt("bookmarkCnt"),
                        getUserMyProductsRes),
                getUserMyParams);

    }

    // 카카오 소셜로그인 이메일 중복 체크
    public int checkKakaoUser(String email) {
            String checkKakaoUserQuery = "select exists(select email\n" +
                    "from KakaoUsers\n" +
                    "where KakaoUsers.email = ?) emailExist";
            String checkKakaoUserPrams = email;

            return this.jdbcTemplate.queryForObject(checkKakaoUserQuery,
                    int.class,
                    checkKakaoUserPrams);
    }

    // 카카오 소셜 로그인 ( 이메일 존재하는경우 )
    public int kakaoLogIn(String email) {
            String kakaoLogInQuery = "select KakaoUsers.id\n" +
                    "from KakaoUsers\n" +
                    "where KakaoUsers.email = ?";
            String kakaoLogInParams = email;

            return this.jdbcTemplate.queryForObject(kakaoLogInQuery,
                    int.class,
                    kakaoLogInParams);
    }

    // 카카오 소셜 회원가입 (이메일 존재 안하는 경우)
    public int kakaoSignUp(String email) {
            String kakaoSignUpQuery = "INSERT INTO bunjang.KakaoUsers (email) \n" +
                    "VALUES (?)";
            String kakaoSignUpParams = email;

            this.jdbcTemplate.update(kakaoSignUpQuery,kakaoSignUpParams);

        String lastInsertUserKakaoIdQuery = "select last_insert_id()";
        int userIdx = this.jdbcTemplate.queryForObject(lastInsertUserKakaoIdQuery, int.class);

        return userIdx;

    }

    public int checkUser(int userIdx) {
        String checkUserQuery = "select exists(select userId\n" +
                "from Users\n" +
                "where userId = ?) checkUser";
        int checkUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserQuery,
                int.class,
                checkUserParams);
    }




}
