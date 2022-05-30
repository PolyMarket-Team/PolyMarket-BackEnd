package kr.polymarket.domain.product.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.polymarket.domain.product.dto.CreateProductArticleDto;
import kr.polymarket.domain.product.dto.ProductArticleDetailDto;
import kr.polymarket.domain.product.service.ProductService;
import kr.polymarket.global.error.ErrorResponse;
import kr.polymarket.global.result.ResultCode;
import kr.polymarket.global.result.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @ApiOperation(value = "상품등록 API", notes = "상품등록 API")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success upload product article"),
            @ApiResponse(code = 400, message = "bad request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "unauthorized", response = ErrorResponse.class),
    })
    @PostMapping("/products")
    public ResponseEntity<ResultResponse<Long>> createProductArticle(@RequestBody @Valid CreateProductArticleDto productArticleDto,
                                                                     @AuthenticationPrincipal UserDetails userDetails) {
        Long productId = productService.createProductArticle(productArticleDto, userDetails);

        ResultResponse<Long> result = ResultResponse.of(ResultCode.PRODUCT_ARTICLE_UPLOAD_SUCCESS,productId);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @ApiOperation(value = "상품조회 API", notes = "상품조회 API")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success load product article"),
            @ApiResponse(code = 400, message = "bad request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "unauthorized", response = ErrorResponse.class),
    })
    @GetMapping("/products/{id}")
    public ResponseEntity<ResultResponse<ProductArticleDetailDto>> productArticleDetail(@PathVariable Long id) {
        ProductArticleDetailDto productArticleDetail = productService.productArticleDetail(id);

        ResultResponse<ProductArticleDetailDto> result = ResultResponse.of(ResultCode.SUCCESS, productArticleDetail);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
        }
    }
