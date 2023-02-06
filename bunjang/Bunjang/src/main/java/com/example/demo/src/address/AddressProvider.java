package com.example.demo.src.address;

import com.example.demo.config.BaseException;
import com.example.demo.src.address.model.GetAddressRes;
import com.example.demo.src.address.model.GetLocationRes;
import com.example.demo.src.address.model.GetSelectAddressRes;
import com.example.demo.utils.JwtService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class AddressProvider {

    private final AddressDao addressDao;
    private final JwtService jwtService;

    @Autowired
    public AddressProvider(AddressDao addressDao, JwtService jwtService) {
        this.addressDao = addressDao;
        this.jwtService = jwtService;
    }
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public List<GetAddressRes> getAddresses(int userId) throws BaseException {
        try {
            return addressDao.getAddresses(userId);
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public GetAddressRes getAddress(int userId, String addressName) throws BaseException {
        try {
            return addressDao.getAddress(userId, addressName);
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public List<GetSelectAddressRes> getSelectAddresses(int userId) throws BaseException {
        try {
            return addressDao.getSelectAddress(userId);
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public GetLocationRes searchLocation(String keyword)throws BaseException{
        String clientId = "2blafdybiv";  //clientId
        String clientSecret = "mkII0UA8VVIe12XO3EbRjNzvTAiescXM9ltrDi6W";  //clientSecret

        try {
            String addr = URLEncoder.encode(keyword, "UTF-8");  //주소입력
            String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + addr; //json
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
            int responseCode = con.getResponseCode();
            System.out.println(responseCode);
            BufferedReader br;
            if(responseCode==200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            JSONParser parser = new JSONParser();         // JSON 파일 읽기
            JSONObject jsonObject = (JSONObject) parser.parse(response.toString());
            JSONArray jsonArray=(JSONArray)jsonObject.get("addresses");
            JSONObject jsonObject1=(JSONObject)jsonArray.get(0);
            String roadAddress= (String) jsonObject1.get("roadAddress");
            String jibunAddress= (String) jsonObject1.get("jibunAddress");

            return new GetLocationRes(roadAddress,jibunAddress);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

}
