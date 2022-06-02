package kr.polymarket.domain.product.repository;

import kr.polymarket.domain.product.entity.ProductFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductFileRepository extends JpaRepository<ProductFile, Long> {

    @Query("select p from ProductFile p where p.product.id is null and p.fileId in :productFileIdList")
    List<ProductFile> findAllById(List<Long>productFileIdList);

}
