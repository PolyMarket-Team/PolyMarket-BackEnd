package kr.polymarket.domain.product.repository;

import kr.polymarket.domain.product.entity.ProductFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductFileRepository extends JpaRepository<ProductFile, Long> {

    @Query("select pf from ProductFile pf where pf.product.id in :productIdList and pf.fileSequence = 1 order by pf.product.id")
    List<ProductFile> findMainImageFileByProductIdIn(List<Long> productIdList);
}
