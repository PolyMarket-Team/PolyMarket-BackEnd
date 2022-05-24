package kr.polymarket.domain.product.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductArticleDetailDto {
    private String title;
    private String content;
    private List<Long> productFileList;
    private String category;
    private int price;
    private int viewNum;
}
