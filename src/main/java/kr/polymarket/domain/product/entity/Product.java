package kr.polymarket.domain.product.entity;

import kr.polymarket.domain.DateBaseEntity;
import kr.polymarket.domain.product.dto.CreateProductArticleRequestDto;
import kr.polymarket.domain.product.dto.ProductArticleDetailResponseDto;
import kr.polymarket.domain.product.dto.ProductCategory;
import kr.polymarket.domain.product.dto.ProductListItemResult;
import kr.polymarket.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuperBuilder
@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Product extends DateBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "product_title")
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false, name = "product_content")
    private String content;

    @Column(nullable = false, name = "product_price")
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "product_wish_num")
    private int wishNum;

    @Column(nullable = false, name = "product_view_num")
    private int viewNum;

    @Column(name = "product_chat_num")
    private int chatNum;

    @Column(nullable = false, name = "product_status")
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<ProductFile> productFileList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Product createProductArticle(User user, CreateProductArticleRequestDto createProductArticleDto, Category category) {
        return Product.builder()
                .title(createProductArticleDto.getTitle())
                .content(createProductArticleDto.getContent())
                .price(createProductArticleDto.getPrice())
                .viewNum(0)
                .wishNum(0)
                .chatNum(0)
                .user(user)
                .category(category)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .status(ProductStatus.NORMAL)
                .build();
    }

    public ProductArticleDetailResponseDto toProductArticleDetail() {
        return ProductArticleDetailResponseDto.builder()
                .title(this.getTitle())
                .category(this.getCategory())
                .price(this.getPrice())
                .content(this.getContent())
                .productFileList(this.getProductFileList().stream().map(ProductFile::getFileUrl).collect(Collectors.toList()))
                .viewNum(this.getViewNum())
                .chatNum(this.getChatNum())
                .wishNum(this.getWishNum())
                .build();
    }
}
