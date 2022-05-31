package kr.polymarket.domain.product.repository;

import kr.polymarket.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from product p join fetch p.category join fetch p.productFileList join fetch p.user where p.id = :id and p.status <> kr.polymarket.domain.product.entity.ProductStatus.DELETE")
    Optional<Product> findProductDetailById(Long id);
}
