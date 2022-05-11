package kr.polymarket.domain.product.entity;

import javax.persistence.*;

@Entity
@Table(name = "product_image")
public class ProductImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer listNum;

    @Column(nullable = false)
    private String imageUrl;

    private final static String supportedExtension[] = {"jpg", "jpeg", "png"};

}
