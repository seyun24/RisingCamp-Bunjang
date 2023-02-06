package com.example.demo.src.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController

@RequestMapping("/bunjang/products")

public class ProductController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ProductProvider productProvider;
    @Autowired
    private final ProductService productService;
    @Autowired
    private final JwtService jwtService;

    public ProductController(ProductProvider productProvider, ProductService productService,JwtService jwtService) {
        this.productProvider = productProvider;
        this.productService = productService;
        this.jwtService = jwtService;
    }

    /**
     * 유저 jwt 조회
     * [GET] /:userIdx/jwt
     */
    @ResponseBody
    @GetMapping("/{userIdx}/jwt")
    public BaseResponse<String> getUserJwt(@PathVariable("userIdx") int userIdx) {
        String useJwt = productService.getUserJwt(userIdx);
        return new BaseResponse<>(useJwt);
    }

 /* 상품 상세 페이지 조회 API
     * [GET] /:productIdx
     */

    @ResponseBody
    @GetMapping("/{productIdx}/{userIdx}")
    public BaseResponse<GetProductDetailRes> getProductDetail(@PathVariable("productIdx") int productIdx,@PathVariable("userIdx") int userIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            
        GetProductDetailRes getProductDetailRes = productProvider.getProductDetail(productIdx,userIdx);

        return new BaseResponse<>(getProductDetailRes);

    } catch (BaseException exception) {
        return new BaseResponse<>((exception.getStatus()));
    }
}

    /* 메인화면 추천 상품 조회 API
     * [GET]
            */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetMainProductsRes>> getMainProducts() {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            List<GetMainProductsRes> getMainProductsRes = productProvider.getMainProducts();

            return new BaseResponse<>(getMainProductsRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    /**
     * 상품 등록 태그 조회 API
     * [GET] /:userIdx/new-product/tags/:tagWord
     */
    @ResponseBody
    @GetMapping("/{userIdx}/new-product/tags/{tagWord}")
    public BaseResponse<List<GetTagsRes>> getTags(@PathVariable("userIdx") int userIdx,@PathVariable String tagWord) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetTagsRes> getTagsRes = productProvider.getTags(tagWord);
            return new BaseResponse<>(getTagsRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 등록 첫번째 카테고리 조회
     * [GET] /:userIdx/new-product/categories
     */
    @ResponseBody
    @GetMapping("/{userIdx}/new-product/categories/first")
    public BaseResponse<List<GetFirstCategoryRes>> getFirstCategory(@PathVariable("userIdx") int userIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetFirstCategoryRes> getFirstCategoryRes = productProvider.getFirstCategory();
            return new BaseResponse<>(getFirstCategoryRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 상품 등록 두번째 카테고리 조회
     * [POST] /:userIdx/new-product/categories/:firstIdx
     */
    @ResponseBody
    @GetMapping("/{userIdx}/new-product/categories/{firstIdx}")
    public BaseResponse<List<GetSecondCategoryRes>> getSecondCategory(@PathVariable("userIdx") int userIdx,@PathVariable("firstIdx") int firstIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if ( firstIdx < 1 && firstIdx > 28) {
                return new BaseResponse<>(INVALID_FIRSTCATEGORYID);
            }
            List<GetSecondCategoryRes> getSecondCategoryRes = productProvider.getSecondCategory(firstIdx);
            return new BaseResponse<>(getSecondCategoryRes);

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 등록 세번째 카테고리 조회!
     * [POST] /:userIdx/new-product/categories/:firstIdx/:secondIdx
     */
    @ResponseBody
    @GetMapping("/{userIdx}/new-product/categories/{firstIdx}/{secondIdx}")
    public BaseResponse<List<GetThirdCategoryRes>> getThirdCategory(@PathVariable("userIdx") int userIdx,@PathVariable("firstIdx") int firstIdx,@PathVariable("secondIdx") int secondIdx ) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if ( firstIdx < 1 || firstIdx > 28) {
                return new BaseResponse<>(INVALID_FIRSTCATEGORYID);
            }
            if ( secondIdx < 14 || secondIdx > 208) {
                return new BaseResponse<>(INVALID_SECONDCATEGORYID);
            }
            List<GetThirdCategoryRes> getThirdCategoryRes = productProvider.getThirdCategory(firstIdx,secondIdx);

            return new BaseResponse<>(getThirdCategoryRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 등록 API
     * [POST] /bunjang/products/:userIdx/new-product
     */
    @ResponseBody
    @PostMapping("/{userIdx}/new-product")
    public BaseResponse<String> postProduct(@PathVariable("userIdx") int userIdx,@RequestBody PostProductReq postProduct) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if (postProduct.getUrl1() == null) {
                return new BaseResponse<>(POST_PRODUCT_EMPTY_IMG);
            }
            if (postProduct.getProductName() == null) {
                return new BaseResponse<>(POST_PRODUCT_EMPTY_NAME);
            }
            if(postProduct.getFirstCategoryName() == null) {
                return new BaseResponse<>(POST_PRODUCT_EMPTY_FIRSTCATEGORYNAME);
            }
            if (postProduct.getDeliveryTip() == null) {
                return new BaseResponse<>(POST_PRODUCT_EMPTY_DELIVERYTIP);
            }
            if (postProduct.getCount() == 0) {
                return new BaseResponse<>(POST_PRODUCT_EMPTY_COUNT);
            }
            if (postProduct.getProductStatus() == null) {
                return new BaseResponse<>(POST_PRODUCT_EMPTY_STATUS);
            }
            if (postProduct.getTrade() == null) {
                return new BaseResponse<>(POST_PRODUCT_EMPTY_TRADE);
            }
            if (postProduct.getDescription() == null ) {
                return new BaseResponse<>(POST_PRODUCT_EMPTY_DESCRIPTION);
            }
            if (postProduct.getSafePay() == null ) {
                return new BaseResponse<>(POST_PRODUCT_EMPTY_SAFEPAY);
            }
            productService.postProduct(userIdx, postProduct);

            String result = "상품 등록이 완료되었습니다.";
                return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/searches/{keyword}")
    public BaseResponse<List<GetMainProductsRes>> searchProduct(@PathVariable("keyword") String keyword) {
        try {
            return new BaseResponse<>(productProvider.getSearch(keyword));

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/searches/products-keywords/{keyword}")
    public BaseResponse<List<GetKeywordRes>> getKeywrods(@PathVariable("keyword") String keyword) {
        try {
            return new BaseResponse<>(productProvider.getKeywords(keyword));

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/searches/store-keywords/{keyword}")
    public BaseResponse<List<GetStoreKeywordRes>> getStoreKeywords(@PathVariable("keyword") String keyword) {
        try {
            return new BaseResponse<>(productProvider.getStoreKeywords(keyword));

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
