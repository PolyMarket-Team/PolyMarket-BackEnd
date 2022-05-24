package kr.polymarket.domain.product.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.polymarket.domain.product.dto.ProductListItemResult;
import kr.polymarket.domain.product.dto.ProductListRequestDto;
import kr.polymarket.domain.product.service.ProductService;
import kr.polymarket.global.error.ErrorResponse;
import kr.polymarket.global.result.ResultCode;
import kr.polymarket.global.result.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @ApiOperation(value = "상품 리스트 조회 API", notes = "상품 리스트 조회 API: 카테고리 필터 제공, 작성일 기준으로 내림차순으로 최신 상품 리스트를 반환, 상품아이디를 기준으로 페이징, 페이지 단위는 20으로 고정")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success result for search"),
            @ApiResponse(code = 400, message = "bad request", response = ErrorResponse.class)
    })
    @GetMapping("/products")
    public ResponseEntity<ResultResponse<List<ProductListItemResult>>> productList(@Valid ProductListRequestDto productListRequest) {
        List<ProductListItemResult> productListResult = productService.findProductList(productListRequest);
        ResultResponse<List<ProductListItemResult>> result = ResultResponse.of(ResultCode.SUCCESS, productListResult);
        return ResponseEntity.status(result.getStatus())
                .body(result);
    }

}
