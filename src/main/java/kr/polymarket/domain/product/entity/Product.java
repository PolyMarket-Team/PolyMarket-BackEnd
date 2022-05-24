package kr.polymarket.domain.product.entity;

import kr.polymarket.domain.DateBaseEntity;
import kr.polymarket.domain.product.dto.CreateProductArticleDto;
import kr.polymarket.domain.product.dto.ProductArticleDetailDto;
import kr.polymarket.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuperBuilder
@Entity(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static Product createProductArticle(long userId, CreateProductArticleDto createProductArticleDto, Category category) {
        return Product.builder()
                .title(createProductArticleDto.getTitle())
                .content(createProductArticleDto.getContent())
                .price(createProductArticleDto.getPrice())
                .viewNum(0)
                .wishNum(0)
                .category(category)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .status(ProductStatus.NORMAL)
                .build();
    }

    public ProductArticleDetailDto toProductArticleDetail(){
        return ProductArticleDetailDto.builder()
                .title(this.getTitle())
                .category(this.getCategory().getName())
                .price(this.getPrice())
                .content(this.getContent())
                .productFileList(this.getProductFileList().stream().map(ProductFile::getFileId).collect(Collectors.toList()))
                .build();
    }
}