package kr.polymarket.domain.product.service;

import kr.polymarket.domain.product.dto.CreateProductArticleRequestDto;
import kr.polymarket.domain.product.dto.ProductArticleDetailResponseDto;
import kr.polymarket.domain.product.dto.ProductListItemResult;
import kr.polymarket.domain.product.dto.ProductListRequestDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
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
    @Transactional
    public long createProductArticle(CreateProductArticleRequestDto productArticleDto, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> { throw new UserNotFoundException("존재하지않는 회원입니다."); });

        List<ProductFile> findProductFileList = productFileRepository.findAllById(productArticleDto.getFileIdList());
        if (findProductFileList.size() != productArticleDto.getFileIdList().size()) {
            throw new ProductFileSizeNotCorrespondException();
        }

        Category category = categoryRepository.findById(productArticleDto.getProductCategory().getId())
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


    /**
     * 최근 동록된 상품 리스트
     * @param productListRequest
     * @return
     */
    public List<ProductListItemResult> findProductList(ProductListRequestDto productListRequest) {
        List<Product> findProductList = productRepository.findProductsByNoOffset(productListRequest.getOffset()
                , productListRequest.getCategory() == null ? null : productListRequest.getCategory().getId());

        Map<Product, ProductFile> productFileMap = convertToProductImageMapBy(findProductList.stream()
                .map(Product::getId)
                .collect(Collectors.toList())
        );

        return findProductList
                .stream()
                .map(p -> ProductListItemResult.of(p, productFileMap.get(p).getFileUrl()))
                .collect(Collectors.toList());
    }

    /**
     * 상품별 메인 이미지 맵 변환 메소드
     * @param productIdList
     * @return
     */
    private Map<Product, ProductFile> convertToProductImageMapBy(List<Long> productIdList) {
        return productFileRepository.findMainImageFileByProductIdIn(productIdList)
                .stream()
                .collect(Collectors.toMap(ProductFile::getProduct, Function.identity()));
    }
}
