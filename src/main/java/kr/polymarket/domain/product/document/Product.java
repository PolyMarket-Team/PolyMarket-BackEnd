package kr.polymarket.domain.product.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Document(indexName = "product_search_idx", createIndex = false)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Field(name = "product_title")
    private String productTitle;

    @Field(name = "product_content")
    private String productContent;

    @Field(name = "category_id")
    private Integer categoryId;

}
