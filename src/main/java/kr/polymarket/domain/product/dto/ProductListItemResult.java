package kr.polymarket.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.polymarket.domain.product.entity.Category;
import kr.polymarket.domain.product.entity.Product;
import lombok.*;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ApiModel(description = "상품 검색 결과 모델")
public class ProductListItemResult {

    @ApiModelProperty(name = "상품 아이디", notes = "상품 아이디, 식별자", example = "1000")
    private Long id;

    @ApiModelProperty(name = "상품 제목", notes = "상품 제목", example = "아이폰 팔아요~")
    private String title;

    @ApiModelProperty(name = "상품 가격", notes = "상품 가격(0~1억원)", example = "130000")
    private Integer price;

    @ApiModelProperty(name = "상품 카테고리", notes = "상품 카테고리")
    private Category category;

    @ApiModelProperty(name = "관심상품 등록수", notes = "관심상품 등록수", example = "10")
    private Integer wishNum;

    @ApiModelProperty(name = "상품 조회수", notes = "상품 조회수", example = "10")
    private Integer viewNum;

    @ApiModelProperty(name = "상품 채팅수", notes = "상품 채팅수", example = "5")
    private Integer chatNum;

    @ApiModelProperty(name = "상품 메인이미지 url", notes = "상품 메인이미지 url", example = "https://polymarket-bucket.s3.ap-northeast-2.amazonaws.com/5Fs23OEk2310ASskBx.jpg")
    private String mainImageUrl;

    @ApiModelProperty(name = "상품 등록자 사용자 아이디", notes = "상품 등록자 사용자 아이디", example = "3")
    private Long userId;

    @ApiModelProperty(name = "상품 등록일", notes = "상품 등록일(한국시간 KST 기준)", example = "2022-01-01 12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @ApiModelProperty(name = "상품 수정일", notes = "상품 수정일(한국시간 KST 기준)", example = "2022-01-01 12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

    public static ProductListItemResult of(Product product) {
        return ProductListItemResult.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .category(product.getCategory())
                .wishNum(product.getWishNum())
                .viewNum(product.getViewNum())
                .chatNum(product.getChatNum())
                .userId(product.getUser().getId())
                .createDate(product.getCreateDate())
                .updateDate(product.getUpdateDate())
                .mainImageUrl(CollectionUtils.isEmpty(product.getProductFileList()) ? null : product.getProductFileList().get(0).getFileUrl())
                .build();
    }

}
