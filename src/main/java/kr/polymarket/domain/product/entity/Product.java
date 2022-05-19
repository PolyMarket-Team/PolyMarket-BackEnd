package kr.polymarket.domain.product.entity;

import kr.polymarket.domain.DateBaseEntity;
import kr.polymarket.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false, name = "product_wish_num")
    private int wishNum;

    @Column(nullable = false, name = "product_view_num")
    private int viewNum;

    @Column(nullable = false, name = "product_chat_num")
    private int chatNum;

    @Column(nullable = false, name = "product_status")
    private String status;

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<ProductFile> productFileList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}