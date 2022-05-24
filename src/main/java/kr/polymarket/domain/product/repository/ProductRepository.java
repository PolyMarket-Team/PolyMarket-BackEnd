package kr.polymarket.domain.product.repository;

import kr.polymarket.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductQueryDslRepository {

    @Query("select p " +
            "from Product p " +
            "join fetch p.category " +
            "join fetch p.productFileList " +
            "where p.id in :productIdList")
    List<Product> findProductsByIdIn(List<Long> productIdList);
}
