package kr.polymarket.domain.product.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@Builder
@Getter
@ApiModel(value = "게시글 조회")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductArticleDetailDto {
    private String title;
    private String content;
    private List<String> productFileList;
    private String category;
    private int price;
    private int viewNum;
    private int chatNum;
    private int wishNum;
}
