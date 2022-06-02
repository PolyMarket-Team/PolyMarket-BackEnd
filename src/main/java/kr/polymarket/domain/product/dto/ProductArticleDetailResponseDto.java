package kr.polymarket.domain.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.polymarket.domain.product.entity.Category;
import lombok.*;

import java.util.List;

@Builder
@Getter
@ApiModel(value = "게시글 조회 응답모델")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductArticleDetailResponseDto {

    @ApiModelProperty(name = "상품 제목", notes = "상품 글 제목", example = "안녕하세요 상품 팝니다!!~~")
    private String title;

    @ApiModelProperty(name = "상품 내용", notes = "상품 글 내용", example = "상품을 팔고 있어요 이 제품은 아이폰 입니다 싸게 팔아요")
    private String content;

    @ApiModelProperty(name = "상품 이미지", notes = "상품 글 이미지", example = "https://polymarket-bucket.s3.ap-northeast-2.amazonaws.com/5Fs23OEk2310ASskBx.jpg")
    private List<String> productFileList;

    @ApiModelProperty(name = "상품 카테고리", notes = "상품 카테고리")
    private Category category;

    @ApiModelProperty(name = "상품가격", notes = "상품가격", example = "10000")
    private int price;

    @ApiModelProperty(name = "상품 조회수", notes = "상품 조회수", example = "3")
    private int viewNum;

    @ApiModelProperty(name = "상품 채팅수", notes = "상품 채팅수", example = "3")
    private int chatNum;

    @ApiModelProperty(name = "상품 찜수", notes = "상품 찜수", example = "3")
    private int wishNum;
}
