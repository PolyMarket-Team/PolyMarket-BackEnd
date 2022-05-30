package kr.polymarket.domain.product.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import kr.polymarket.domain.product.entity.Product;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static kr.polymarket.domain.product.entity.QProduct.product;

public class ProductQueryDslRepositoryImpl extends QuerydslRepositorySupport implements ProductQueryDslRepository {

    public ProductQueryDslRepositoryImpl() {
        super(Product.class);
    }

    @Override
    public List<Product> findProductsByNoOffset(Long productId, Integer categoryId) {
        return from(product)
                .innerJoin(product.category).fetchJoin()
                .where(ltProductId(productId), eqCategoryId(categoryId))
                .orderBy(product.id.desc())
                .limit(PRODUCT_LIST_PAGE_SIZE)
                .fetch();
    }

    private BooleanExpression ltProductId(Long productId) {
        return productId == null ? null : product.id.lt(productId);
    }

    private BooleanExpression eqCategoryId(Integer categoryId) {
        return categoryId == null ? null : product.category.id.eq(categoryId);
    }

}
