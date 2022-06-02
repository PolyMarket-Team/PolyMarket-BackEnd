package kr.polymarket.domain.product.repository;

import kr.polymarket.domain.product.entity.Product;

import java.util.List;

public interface ProductQueryDslRepository {

    List<Product> findProductsByNoOffset(Long productId, Integer categoryId);

    int PRODUCT_LIST_PAGE_SIZE = 20;
}
