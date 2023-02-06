package com.example.demo.src.category;

import com.example.demo.src.product.model.GetMainProductsRes;
import com.example.demo.src.product.model.GetSecondCategoryRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.category.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController

@RequestMapping("/bunjang/categories")

public class CategoryController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CategoryProvider categoryProvider;
    @Autowired
    private final CategoryService categoryService;
    @Autowired
    private final JwtService jwtService;

    public CategoryController(CategoryProvider categoryProvider, CategoryService categoryService, JwtService jwtService) {
        this.categoryProvider = categoryProvider;
        this.categoryService = categoryService;
        this.jwtService = jwtService;
    }

    /**
     * 첫번째 카테고리 상품 조회
     * /:firstIdx
     */
    @ResponseBody
    @GetMapping("/{firstIdx}")
    public BaseResponse<List<GetMainProductsRes>> getFirstCategoryProducts(@PathVariable("firstIdx") int firstIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if ( firstIdx < 1 && firstIdx > 28) {
                return new BaseResponse<>(INVALID_FIRSTCATEGORYID);
            }
            List<GetMainProductsRes> getMainProductsRes = categoryProvider.getFirstCategoryProducts(firstIdx);

            return new BaseResponse<>(getMainProductsRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 두번째 카테고리 상품 조회
     * /:firstIdx/:secondIdx
     */
    @ResponseBody
    @GetMapping("/{firstIdx}/{secondIdx}")
    public BaseResponse<List<GetMainProductsRes>> getSecondCategoryProducts(@PathVariable("firstIdx") int firstIdx, @PathVariable("secondIdx") int secondIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if ( firstIdx < 1 || firstIdx > 28) {
                return new BaseResponse<>(INVALID_FIRSTCATEGORYID);
            }
            List<GetMainProductsRes> getMainProductsRes = categoryProvider.getSecondCategoryProducts(firstIdx,secondIdx);

            return new BaseResponse<>(getMainProductsRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 세번째 카테고리 상품 조회
     * /:firstIdx/:secondIdx/:thirdIdx
     */
    @ResponseBody
    @GetMapping("/{firstIdx}/{secondIdx}/{thirdIdx}")
    public BaseResponse<List<GetMainProductsRes>> getThirdCategoryProducts(@PathVariable("firstIdx") int firstIdx,@PathVariable("secondIdx") int secondIdx,@PathVariable("thirdIdx") int thirdIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            
            if ( firstIdx < 1 || firstIdx > 28) {
                return new BaseResponse<>(INVALID_FIRSTCATEGORYID);
            }

            List<GetMainProductsRes> getMainProductsRes = categoryProvider.getThirdCategoryProducts(firstIdx,secondIdx,thirdIdx);

            return new BaseResponse<>(getMainProductsRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
