package kr.polymarket.domain.product.repository;

import kr.polymarket.domain.product.dto.SearchWithPITResult;

import java.io.IOException;

public interface ProductSearchRepository {

    SearchWithPITResult searchProductIdList(String query, Integer categoryId, int page, String pit) throws IOException;

    String INDEX_NAME = "product_search_idx";
    int PAGE_SIZE = 20;
}
