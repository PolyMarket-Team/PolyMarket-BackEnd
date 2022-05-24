package kr.polymarket.domain.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

/**
 * 상품 검색 요청 dto
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ApiModel(description = "상품 검색 요청 모델")
public class ProductSearchRequestDto {

    @NotBlank(message = "검색어는 필수 항목입니다.")
    @Length(min = 2, message = "검색어는 두 글자 이상이어야 합니다.")
    @ApiParam(value = "검색어: 상풍의 제목과 내용기반으로 검색", required = true, example = "아이폰 12")
    private String query;

    @ApiParam(value = "상품 카테고리(요청이 없는 경우 전체 카테고리에 대해서 검색)", example = "HOME_APPLIANCES")
    private ProductCategory category;

    @Range(min = 0, max = 20, message = "페이지 번호는 0 이상 20 이하이어야 합니다.")
    @ApiParam(value = "페이지(최대 20페이지까지 지원), 디폴트 값: 0", example = "2")
    private Integer page;

    @ApiParam(value = "PIT(Point In Time): 검색 시점 기준 값, 검색 페이징을 위한 스냅샷 아이디 @@ 주의: 첫번째 페이지 요청시에는 빈값으로 요청",
            example = "39K1AwEScHJvZHVjdF9zZWFyY2hfaWR4FjJZMzZVUXBvUzVpVzNodWIyZ2VZQmcAFkhnVTRsMTlNU3EtUURXeEt5amdlWlEAAAAAAAAAEYQWMlotLUltV1ZTVy1rWG9BcE5rdVh0UQABFjJZMzZVUXBvUzVpVzNodWIyZ2VZQmcAAA==")
    private String pit;

}