package kr.polymarket.domain.product.service;

import kr.polymarket.domain.product.dto.CreateProductArticleDto;
import kr.polymarket.domain.product.dto.ProductArticleDetailDto;
import kr.polymarket.domain.product.entity.Category;
import kr.polymarket.domain.product.entity.Product;
import kr.polymarket.domain.product.exception.CategoryNotFoundException;
import kr.polymarket.domain.product.exception.ProductNotFoundException;
import kr.polymarket.domain.product.repository.CategoryRepository;
import kr.polymarket.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;


    /**
     * 상품등록
     * @param productArticleDto
     * @param userId
     * @return
     */
    public long createProductArticle(CreateProductArticleDto productArticleDto, long userId) {
        //2 카테고리 저장

        Category category = categoryRepository.findById(productArticleDto.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);
        //3 정보들 주입 객체생성
        Product product = Product.createProductArticle(userId, productArticleDto, category);
        //4 이미지랑 연관관계 매핑

        //
        productRepository.save(product);
        //7 결과 리턴
        return product.getId();
    }

    /** TODO
     *
     * 상품 조회
     */
    @Transactional(readOnly = true)
    public ProductArticleDetailDto productDetail(long Id) {
        Product product = productRepository.findProductDetailById(Id)
                .orElseThrow(() -> {
                    throw new ProductNotFoundException();
                });
        return product.toProductArticleDetail();
    }




    /**
     * 상품 삭제
     */

    /**
     * 상품 수정
     */
}
