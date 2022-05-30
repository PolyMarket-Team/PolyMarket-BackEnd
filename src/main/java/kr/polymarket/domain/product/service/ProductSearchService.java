package kr.polymarket.domain.product.service;

import kr.polymarket.domain.product.dto.ProductListItemResult;
import kr.polymarket.domain.product.dto.ProductSearchRequestDto;
import kr.polymarket.domain.product.dto.ProductSearchResult;
import kr.polymarket.domain.product.dto.SearchWithPITResult;
import kr.polymarket.domain.product.entity.Product;
import kr.polymarket.domain.product.repository.ProductRepository;
import kr.polymarket.domain.product.repository.ProductSearchCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductSearchService {

    private final ProductSearchCustomRepository productSearchCustomRepository;
    private final ProductRepository productRepository;

    /**
     * 키워드를 바탕으로 상품리스트를 검색하는 메소드
     * @param productSearchRequest
     * @return
     */
    public ProductSearchResult searchProductList(ProductSearchRequestDto productSearchRequest) throws IOException {
        SearchWithPITResult searchWithPITResult = productSearchCustomRepository.searchProductIdList(productSearchRequest.getQuery(),
                CollectionUtils.isEmpty(productSearchRequest.getCategoryList()) ?
                        Collections.emptyList() :
                        productSearchRequest.getCategoryList().stream()
                                .map(ProductSearchRequestDto.ProductCategory::getId)
                                .collect(Collectors.toList()),
                productSearchRequest.getPage(), productSearchRequest.getPit());

        List<Product> productList = productRepository.findProductsByIdIn(searchWithPITResult.getRetrievedProductIdList());
        return ProductSearchResult.builder()
                .pit(searchWithPITResult.getPit())
                .productResultList(productList.stream().map(ProductListItemResult::of).collect(Collectors.toList()))
                .build();
    }
}
