package kr.polymarket.domain.product.service;


import kr.polymarket.domain.product.dto.ProductListItemResult;
import kr.polymarket.domain.product.dto.ProductListRequestDto;
import kr.polymarket.domain.product.entity.Product;
import kr.polymarket.domain.product.entity.ProductFile;
import kr.polymarket.domain.product.repository.ProductFileRepository;
import kr.polymarket.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductFileRepository productFileRepository;

    /**
     * 최근 동록된 상품 리스트
     * @param productListRequest
     * @return
     */
    public List<ProductListItemResult> findProductList(ProductListRequestDto productListRequest) {
        List<Product> findProductList = productRepository.findProductsByNoOffset(productListRequest.getOffset()
                , productListRequest.getCategory() == null ? null : productListRequest.getCategory().getId());

        Map<Product, ProductFile> productFileMap = convertToProductImageMapBy(findProductList.stream()
                .map(Product::getId)
                .collect(Collectors.toList())
        );

        return findProductList
                .stream()
                .map(p -> ProductListItemResult.of(p, productFileMap.get(p).getFileUrl()))
                .collect(Collectors.toList());
    }

    /**
     * 상품별 메인 이미지 맵 변환 메소드
     * @param productIdList
     * @return
     */
    private Map<Product, ProductFile> convertToProductImageMapBy(List<Long> productIdList) {
        return productFileRepository.findMainImageFileByProductIdIn(productIdList)
                .stream()
                .collect(Collectors.toMap(ProductFile::getProduct, Function.identity()));
    }

}
