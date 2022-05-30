package kr.polymarket.domain.product.repository;

import kr.polymarket.domain.product.dto.SearchWithPITResult;

import java.io.IOException;
import java.util.List;

public interface ProductSearchRepository {

    SearchWithPITResult searchProductIdList(String query, List<Integer> categoryIdList, int page, String pit) throws IOException;

    String INDEX_NAME = "product_search_idx";
    int PAGE_SIZE = 20;
}
