package com.example.demo.src.sale;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.order.model.GetOrderRes;
import com.example.demo.src.sale.model.GetSaleRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/bunjang/sales")
public class SaleController {

    @Autowired
    private final SaleProvider saleProvider;
    @Autowired
    private final SaleService saleService;
    @Autowired
    private final JwtService jwtService;


    public SaleController(SaleProvider saleProvider, SaleService saleService, JwtService jwtService) {
        this.saleProvider = saleProvider;
        this.saleService = saleService;
        this.jwtService = jwtService;
    }

    //전체 판매내역 조회
    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<List<GetSaleRes>> getSales(@PathVariable("userId") int userId){
        try {
                //jwt에서 idx 추출.
                int userIdxByJwt = jwtService.getUserIdx();
                //userIdx와 접근한 유저가 같은지 확인
                if(userId != userIdxByJwt){
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
            return new BaseResponse<>(saleProvider.getSales(userId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //취소/환불 구매내역 조회
    @ResponseBody
    @GetMapping("cancels/{userId}")
    public BaseResponse<List<GetSaleRes>> getOrdersCancel(@PathVariable("userId") int userId){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            return new BaseResponse<>(saleProvider.getSalesCancel(userId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //진행중 판매내역 조회
    @ResponseBody
    @GetMapping("progresses/{userId}")
    public BaseResponse<List<GetSaleRes>> getOrdersProgress(@PathVariable("userId") int userId){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            return new BaseResponse<>(saleProvider.getSalesProgress(userId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //완료된 판매내역 조회
    @ResponseBody
    @GetMapping("/completes/{userId}")
    public BaseResponse<List<GetSaleRes>> getOrdersComplete(@PathVariable("userId") int userId){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            return new BaseResponse<>(saleProvider.getSalesComplete(userId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
