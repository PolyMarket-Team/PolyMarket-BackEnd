package kr.polymarket.domain.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@ApiModel(value = "게시글 조회 응답모델")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductArticleDetailResponseDto {

    @ApiModelProperty(name = "상품 제목", notes = "상품 글 제목")
    private String title;

    @ApiModelProperty(name = "상품 내용", notes = "상품 글 내용")
    private String content;

    @ApiModelProperty(name = "상품 이미지", notes = "상품 글 이미지")
    private List<String> productFileList;

    @ApiModelProperty(name = "상품 카테고리", notes = "상품 카테고리")
    private String category;

    @ApiModelProperty(name = "상품가격", notes = "상품가격")
    private int price;

    @ApiModelProperty(name = "상품 조회수", notes = "상품 조회수")
    private int viewNum;

    @ApiModelProperty(name = "상품 채팅수", notes = "상품 채팅수")
    private int chatNum;

    @ApiModelProperty(name = "상품 찜수", notes = "상품 찜수")
    private int wishNum;
}
