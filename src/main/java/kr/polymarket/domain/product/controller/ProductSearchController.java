package kr.polymarket.domain.product.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.polymarket.domain.product.dto.ProductSearchRequestDto;
import kr.polymarket.domain.product.dto.ProductSearchResult;
import kr.polymarket.domain.product.service.ProductSearchService;
import kr.polymarket.global.error.ErrorResponse;
import kr.polymarket.global.result.ResultCode;
import kr.polymarket.global.result.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductSearchController {

    private final ProductSearchService productSearchService;

    @ApiOperation(value = "상품검색 API", notes = "상품검색 API: 키워드 및 카테고리 필터제공, 페이징은 20페이지까지 제공, 검색결과는 페이지당 20개로 고정")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success result for search"),
            @ApiResponse(code = 400, message = "bad request", response = ErrorResponse.class)
    })
    @GetMapping(path = "/search/product")
    public ResponseEntity<ResultResponse<ProductSearchResult>> search(@Valid ProductSearchRequestDto searchRequest) throws IOException {
        ProductSearchResult searchResult = productSearchService.searchProductList(searchRequest);
        ResultResponse<ProductSearchResult> result = ResultResponse.of(ResultCode.SUCCESS, searchResult);
        return ResponseEntity.status(result.getStatus())
                .body(result);
    }

}
