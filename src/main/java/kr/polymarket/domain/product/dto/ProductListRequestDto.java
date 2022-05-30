package kr.polymarket.domain.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ApiModel(description = "상품 리스트 조회 요청 모델")
public class ProductListRequestDto {

    @Range(min = 0, message = "오프셋 번호는 0 이상 이어야 합니다.")
    @ApiParam(value = "오프셋 번호, 페이징의 기준(상품번호)", example = "2")
    private Long offset;

    @ApiParam(value = "상품 카테고리(요청이 없는 경우 전체 카테고리에 대해서 검색)", example = "HOME_APPLIANCES")
    private ProductCategory category;
}
