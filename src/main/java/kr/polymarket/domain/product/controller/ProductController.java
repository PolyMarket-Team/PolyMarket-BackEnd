package kr.polymarket.domain.product.controller;

import kr.polymarket.domain.product.dto.CreateProductArticleDto;
import kr.polymarket.domain.product.dto.ProductArticleDetailDto;
import kr.polymarket.domain.product.service.ProductService;
import kr.polymarket.global.result.ResultCode;
import kr.polymarket.global.result.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ResultResponse<Long>> createProductArticle(@RequestBody @Valid CreateProductArticleDto productArticleDto, long userId) {
        Long productId = productService.createProductArticle(productArticleDto, userId);

        ResultResponse<Long> result = ResultResponse.of(ResultCode.PRODUCT_ARTICLE_UPLOAD_SUCCESS,productId);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ResultResponse<ProductArticleDetailDto>> ProductArticleDetail(@PathVariable Long id) {
        ProductArticleDetailDto productArticleDetail = productService.productDetail(id);

        ResultResponse<ProductArticleDetailDto> result = ResultResponse.of(ResultCode.SUCCESS, productArticleDetail);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
        }
    }
