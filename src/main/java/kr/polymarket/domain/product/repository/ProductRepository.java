package kr.polymarket.domain.product.repository;

import kr.polymarket.domain.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductImage, Long> {
}
