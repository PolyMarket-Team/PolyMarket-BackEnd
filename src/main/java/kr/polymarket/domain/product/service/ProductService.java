package kr.polymarket.domain.product.service;

import kr.polymarket.domain.product.dto.CreateProductArticleRequestDto;
import kr.polymarket.domain.product.dto.ProductArticleDetailResponseDto;
import kr.polymarket.domain.product.entity.Category;
import kr.polymarket.domain.product.entity.Product;
import kr.polymarket.domain.product.entity.ProductFile;
import kr.polymarket.domain.product.exception.ProductFileSizeNotCorrespondException;
import kr.polymarket.domain.product.exception.ProductNotFoundException;
import kr.polymarket.domain.product.repository.CategoryRepository;
import kr.polymarket.domain.product.repository.ProductFileRepository;
import kr.polymarket.domain.product.repository.ProductRepository;
import kr.polymarket.domain.user.entity.User;
import kr.polymarket.domain.user.exception.UserNotFoundException;
import kr.polymarket.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductFileRepository productFileRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 상품등록
     * @param productArticleDto
     * @param
     * @return
     */
    public long createProductArticle(CreateProductArticleRequestDto productArticleDto, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> { throw new UserNotFoundException("존재하지않는 회원입니다."); });

        List<ProductFile> findProductFileList = productFileRepository.findAllById(productArticleDto.getFileIdList());
        if (findProductFileList.size() != productArticleDto.getFileIdList().size()) {
            throw new ProductFileSizeNotCorrespondException();
        }

        Category category = categoryRepository.findById(productArticleDto.getCategoryId())
                .orElseThrow(() -> { throw new IllegalStateException("카테고리가 존재하지 않습니다."); });

        Product product = Product.createProductArticle(user, productArticleDto, category);
        productRepository.save(product);

        findProductFileList.forEach(productFile -> {
            productFile.setFileSequence(findProductFileList.indexOf(productFile) + 1);
            productFile.setProduct(product);
        });

        return product.getId();
    }

    /**
     * TODO
     * 상품 조회
     */
    @Transactional(readOnly = true)
    public ProductArticleDetailResponseDto productArticleDetail(long Id) {
        Product product = productRepository.findProductDetailById(Id)
                .orElseThrow(() -> { throw new ProductNotFoundException(); });

        return product.toProductArticleDetail();
    }

    /**
     * 상품 삭제
     */



    /**
     * 상품 수정
     * todo 수정할 때 이미 있던 파일들 싹다 isDelete 처리하기
     */
}
