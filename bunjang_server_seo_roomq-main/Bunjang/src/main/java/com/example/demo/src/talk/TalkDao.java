package com.example.demo.src.talk;

import com.example.demo.src.talk.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository

public class TalkDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 이미 만들어지 채팅방 존재 여부 확인
    public int checkRoomExist(int sendUserIdx, int receiveUserIdx) {
        String checkRoomExistQuery = "select exists(select roomId\n" +
                "from Chat\n" +
                "where ( sendUserId = ? and receiveUserId = ? ) or ( sendUserId = ? and receiveUserId = ? )) as existRoom";

        Object[] checkRoomExistParams = new Object[]{sendUserIdx, receiveUserIdx, receiveUserIdx, sendUserIdx};

        return this.jdbcTemplate.queryForObject(checkRoomExistQuery,
                int.class,
                checkRoomExistParams);
    }

    // 처음 채팅할때 room부터 새로 만들고 메세지 보내는 메소드
    public int postTalkNew(PostTalkReq postTalkReq) {

        // room 생성성
       String createNewRoomQuery = "INSERT INTO bunjang.Room () VALUES ()";
        this.jdbcTemplate.update(createNewRoomQuery);

        String lastInsertRoomIdQuery = "select last_insert_id()";
        int roomId = this.jdbcTemplate.queryForObject(lastInsertRoomIdQuery, int.class);

        String postTalkNewQuery = "INSERT INTO bunjang.Chat (roomId, sendUserId, receiveUserId, contents) \n" +
                "VALUES (?, ?, ?, ?)";
        Object[] postTalkNewParams = new Object[]{roomId,postTalkReq.getSendUserIdx(), postTalkReq.getReceiveUserIdx(), postTalkReq.getContents()};

        this.jdbcTemplate.update(postTalkNewQuery, postTalkNewParams);
        return roomId;
    }

    // 이미 room이 존재하고 이어서 메세지 보낼때
    public int postTalk(PostTalkReq postTalkReq) {

        String roomIdQuery = "select distinct roomId\n" +
                "from Chat\n" +
                "where roomId = (select distinct roomId\n" +
                "from Chat\n" +
                "where ( sendUserId = ? and receiveUserId = ? ) or ( sendUserId = ? and receiveUserId = ? ))";
        Object[] roomIdParams = new Object[]{postTalkReq.getSendUserIdx(),postTalkReq.getReceiveUserIdx(),
        postTalkReq.getReceiveUserIdx(), postTalkReq.getSendUserIdx()};

        int roomId = this.jdbcTemplate.queryForObject(roomIdQuery,
                int.class,
                roomIdParams);

        String postTalkQuery = "INSERT INTO bunjang.Chat (roomId, sendUserId, receiveUserId, contents)\n" +
                "VALUES (?, ?, ?, ?)";
        Object[] postTalkParams = new Object[]{roomId, postTalkReq.getSendUserIdx(), postTalkReq.getReceiveUserIdx(), postTalkReq.getContents()};

        this.jdbcTemplate.update(postTalkQuery,postTalkParams);
        return roomId;
    }

    // 번개톡 직거래 요청
    public int postDirectTransaction(PostDirectTransactionReq postDirectTransactionReq) {
        // 직거래 요청
        String postDirectTransactionQuery = "INSERT INTO bunjang.DirectTransaction (sendUserId,receiveUserId, saleOrBuy, productId, price, deliveryTip, transactionDate, transactionLocation)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Object[] postDirectTransactionParams = new Object[]{postDirectTransactionReq.getSendUserIdx(),postDirectTransactionReq.getReceiveUserIdx(),
                postDirectTransactionReq.getSaleOrBuy(), postDirectTransactionReq.getProductId(),
        postDirectTransactionReq.getPrice(), postDirectTransactionReq.getDeliveryTip(), postDirectTransactionReq.getTransactionDate(), postDirectTransactionReq.getTransactionLocation()};

        this.jdbcTemplate.update(postDirectTransactionQuery,postDirectTransactionParams);

        String lastInsertDirectTransactionIdQuery = "select last_insert_id()";
        int directTransactionId = this.jdbcTemplate.queryForObject(lastInsertDirectTransactionIdQuery, int.class);

        // 직거래 요청 후 메세지 보내기

        String talkAfterDirectTransactionQuery = "INSERT INTO bunjang.Chat (roomId, sendUserId, receiveUserId, contents, detailContents, productName, method, price)\n" +
                "VALUES ( ( select roomId from (select distinct roomId\n" +
                "from Chat\n" +
                "where roomId = (select distinct roomId\n" +
                "from Chat\n" +
                "where ( sendUserId = ? and receiveUserId = ? ) or ( sendUserId = ? and receiveUserId = ? ))) a ), ?, ?, '직거래를 요청합니다.', '만나서 거래해요',\n" +
                "        (select distinct productName\n" +
                "from DirectTransaction\n" +
                "inner join Products on Products.id = DirectTransaction.productId\n" +
                "where DirectTransaction.productId = ?), '직거래', (select DirectTransaction.price\n" +
                "from DirectTransaction\n" +
                "where DirectTransaction.id = ?))";


        Object[] talkAfterDirectTransactionParams = new Object[]{postDirectTransactionReq.getSendUserIdx(),postDirectTransactionReq.getReceiveUserIdx(),
        postDirectTransactionReq.getReceiveUserIdx(), postDirectTransactionReq.getSendUserIdx(), postDirectTransactionReq.getSendUserIdx(), postDirectTransactionReq.getReceiveUserIdx(),
        postDirectTransactionReq.getProductId(), directTransactionId};

        this.jdbcTemplate.update(talkAfterDirectTransactionQuery,talkAfterDirectTransactionParams);

        return directTransactionId;
    }

    // 번개톡 직거래 요청 정보 수정
    public int modifyDirectTransaction(ModifyDirectTransactionReq modifyDirectTransactionReq) {

        String modifyDirectTransactionQuery = "UPDATE bunjang.DirectTransaction t SET t.productId = ?, t.price = ?, t.deliveryTip = ?\n" +
                "                                     , t.transactionDate = ?, t.transactionLocation = ?\n" +
                "                                   WHERE t.id = ?";
        Object[] modifyDirectTransactionParams = new Object[]{modifyDirectTransactionReq.getProductId(), modifyDirectTransactionReq.getPrice(),
        modifyDirectTransactionReq.getDeliveryTip(),modifyDirectTransactionReq.getTransactionDate(),modifyDirectTransactionReq.getTransactionLocation()
        ,modifyDirectTransactionReq.getDirectTransactionId()};

        this.jdbcTemplate.update(modifyDirectTransactionQuery,modifyDirectTransactionParams);

        // 직거래 요청 정보 수정 후 메세지 보내기
        String talkAfterModifyDirectTransactionQuery = "INSERT INTO bunjang.Chat (roomId, sendUserId, receiveUserId, contents, detailContents, productName, method, price)\n" +
                "VALUES ( ( select roomId from (select distinct roomId\n" +
                "from Chat\n" +
                "where roomId = (select distinct roomId\n" +
                "from Chat\n" +
                "where ( sendUserId = ? and receiveUserId = ? ) or ( sendUserId = ? and receiveUserId = ? ))) a ), ?, ?, '거래가 수정 되었습니다.', '만나서 거래해요',\n" +
                "        (select distinct productName\n" +
                "from DirectTransaction\n" +
                "inner join Products on Products.id = DirectTransaction.productId\n" +
                "where DirectTransaction.productId = ?), '직거래', (select DirectTransaction.price\n" +
                "from DirectTransaction\n" +
                "where DirectTransaction.id = ?))";

        Object[] talkAfterModifyDirectTransactionParams = new Object[]{modifyDirectTransactionReq.getSendUserIdx(), modifyDirectTransactionReq.getReceiveUserIdx(),
        modifyDirectTransactionReq.getReceiveUserIdx(), modifyDirectTransactionReq.getSendUserIdx(),modifyDirectTransactionReq.getSendUserIdx(), modifyDirectTransactionReq.getReceiveUserIdx(),
                modifyDirectTransactionReq.getProductId(), modifyDirectTransactionReq.getDirectTransactionId()};

        return this.jdbcTemplate.update(talkAfterModifyDirectTransactionQuery,talkAfterModifyDirectTransactionParams);
    }

    //직거래 요청 승인
    public int approvalDirectTransaction(ApprovalDirectTransactionReq approvalDirectTransactionReq) {
        // 승인 메세지 보내기
        String approvalDirectTransactionQuery = "INSERT INTO bunjang.Chat (roomId, sendUserId, receiveUserId, contents, detailContents, productName, method, price)\n" +
                "VALUES ( ( select roomId from (select distinct roomId\n" +
                "from Chat\n" +
                "where roomId = (select distinct roomId\n" +
                "from Chat\n" +
                "where ( sendUserId = ? and receiveUserId = ? ) or ( sendUserId = ? and receiveUserId = ? ))) a ), ?, ?, '직거래를 약속합니다.', '상품이 직거래 예약완료로 변경되었습니다.',\n" +
                "        (select distinct productName\n" +
                "from DirectTransaction\n" +
                "inner join Products on Products.id = DirectTransaction.productId\n" +
                "where DirectTransaction.id = ?), '직거래', (select DirectTransaction.price\n" +
                "from DirectTransaction\n" +
                "where DirectTransaction.id = ?))";
        Object[] approvalDirectTransactionPrams = new Object[]{approvalDirectTransactionReq.getSendUserIdx(),approvalDirectTransactionReq.getReceiveUserIdx(),
        approvalDirectTransactionReq.getReceiveUserIdx(), approvalDirectTransactionReq.getSendUserIdx(), approvalDirectTransactionReq.getSendUserIdx(),
        approvalDirectTransactionReq.getReceiveUserIdx(), approvalDirectTransactionReq.getDirectTransactionId(), approvalDirectTransactionReq.getDirectTransactionId()};

        return this.jdbcTemplate.update(approvalDirectTransactionQuery,approvalDirectTransactionPrams);
    }

    // 직거래 상세 정보 조회
    public GetDirectTransactionInfoRes getDirectTransactionInfo(GetDirectTransactionInfoReq getDirectTransactionInfoReq) {

        int directTransactionId = getDirectTransactionInfoReq.getDirectTransactionId();

        String getDirectTransactionInfoQuery = "select distinct ProductImgUrls.productId, url1, productName, format(DirectTransaction.price, '###,###') as price, date_format(DirectTransaction.transactionDate, '%Y년 %m월 %d일') as transactionDate,\n" +
                "       DirectTransaction.transactionLocation, (select storeName\n" +
                "from Stores\n" +
                "inner join Users on Stores.userId = Users.userId\n" +
                "inner join DirectTransaction on DirectTransaction.sendUserId = Users.userId\n" +
                "where DirectTransaction.id = ?) as sendUserStoreName,\n" +
                "    (select phone\n" +
                "from Stores\n" +
                "inner join Users on Stores.userId = Users.userId\n" +
                "inner join DirectTransaction on DirectTransaction.sendUserId = Users.userId\n" +
                "where DirectTransaction.id = ?) as sendUserPhoneNum,\n" +
                "    (select storeName\n" +
                "from Stores\n" +
                "inner join Users on Stores.userId = Users.userId\n" +
                "inner join DirectTransaction on DirectTransaction.receiveUserId = Users.userId\n" +
                "where DirectTransaction.id = ?) as receiveUserStoreName,\n" +
                "    (select phone\n" +
                "from Stores\n" +
                "inner join Users on Stores.userId = Users.userId\n" +
                "inner join DirectTransaction on DirectTransaction.receiveUserId = Users.userId\n" +
                "where DirectTransaction.id = ?) as receiveUserPhoneNum\n" +
                "from DirectTransaction\n" +
                "inner join Products on Products.id = DirectTransaction.productId\n" +
                "inner join ProductImgUrls on ProductImgUrls.productId = Products.id\n" +
                "inner join Users on Users.userId = DirectTransaction.sendUserId or Users.userId = DirectTransaction.receiveUserId\n" +
                "inner join Stores on Stores.userId = Users.userId\n" +
                "where DirectTransaction.id = ?";
        Object[] getDirectTransactionInfoParams = new Object[]{directTransactionId,directTransactionId,directTransactionId,directTransactionId,directTransactionId};

    return this.jdbcTemplate.queryForObject(getDirectTransactionInfoQuery,
            (rs, rowNum) -> new GetDirectTransactionInfoRes(
                    rs.getInt("productId"),
                    rs.getString("url1"),
                    rs.getString("productName"),
                    rs.getString("price"),
                    rs.getString("transactionDate"),
                    rs.getString("transactionLocation"),
                    rs.getString("sendUserStoreName"),
                    rs.getString("sendUserPhoneNum"),
                    rs.getString("receiveUserStoreName"),
                    rs.getString("receiveUserPhoneNum")),
            getDirectTransactionInfoParams);

    }

    // 거래 완료 메세지 전송
    public int directTransactionDone(ApprovalDirectTransactionReq approvalDirectTransactionReq) {

        String directTransactionDoneQuery = "INSERT INTO bunjang.Chat (roomId, sendUserId, receiveUserId, contents, detailContents, productName, method, price)\n" +
                "VALUES ( ( select roomId from (select distinct roomId\n" +
                "from Chat\n" +
                "where roomId = (select distinct roomId\n" +
                "from Chat\n" +
                "where ( sendUserId = ? and receiveUserId = ? ) or ( sendUserId = ? and receiveUserId = ? ))) a ), ?, ?, '거래가 완료 되었습니다.', '서로의 상점에 거래후기를 남길 수 있습니다.',\n" +
                "        (select distinct productName\n" +
                "from DirectTransaction\n" +
                "inner join Products on Products.id = DirectTransaction.productId\n" +
                "where DirectTransaction.id = ?), '직거래', (select DirectTransaction.price\n" +
                "from DirectTransaction\n" +
                "where DirectTransaction.id = ?))";

        Object[] directTransactionDoneParams = new Object[]{approvalDirectTransactionReq.getSendUserIdx(), approvalDirectTransactionReq.getReceiveUserIdx(),
        approvalDirectTransactionReq.getReceiveUserIdx(), approvalDirectTransactionReq.getSendUserIdx(), approvalDirectTransactionReq.getSendUserIdx(),
        approvalDirectTransactionReq.getReceiveUserIdx(), approvalDirectTransactionReq.getDirectTransactionId(), approvalDirectTransactionReq.getDirectTransactionId()};

        return this.jdbcTemplate.update(directTransactionDoneQuery, directTransactionDoneParams);

    }

    // 번개톡 대화 내용 조회
    public List<GetUserChatsRes> getUserChats(int roomIdx) {
        String getUserChatsQuery = "select sendUserId, receiveUserId, contents,detailContents, productName, method,  format(price, '###,###') as price,\n" +
                "       (case when date_format(Chat.createAt, '%p') = 'PM'\n" +
                "            then replace(date_format(Chat.createAt, '%y.%m.%d.%p %h:%i'),'PM','오후')\n" +
                "            when date_format(Chat.createAt, '%p') = 'AM'\n" +
                "            then replace(date_format(Chat.createAt, '%y.%m.%d.%p %h:%i'),'AM','오전')\n" +
                "            end) postTime\n" +
                "from Chat\n" +
                "where Chat.roomId = ?";
        int getUserChatsParams = roomIdx;

        return this.jdbcTemplate.query(getUserChatsQuery,
                (rs, rowNum) -> new GetUserChatsRes(
                        rs.getInt("sendUserId"),
                        rs.getInt("receiveUserId"),
                        rs.getString("contents"),
                        rs.getString("detailContents"),
                        rs.getString("productName"),
                        rs.getString("method"),
                        rs.getString("price"),
                        rs.getString("postTime")),
                getUserChatsParams);
    }



}
