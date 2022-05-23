package kr.polymarket.domain.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ApiModel(description = "상품 검색 결과 모델")
public class ProductSearchResult {

    @ApiModelProperty(name = "PIT(point in time)", notes = "PIT(Point In Time): 검색 시점 기준 값, 검색 페이징을 위한 스냅샷 아이디 @@ 주의: 첫번째 페이지 요청시에는 빈값으로 요청\","
        , example = "39K1AwEScHJvZHVjdF9zZWFyY2hfaWR4FjJZMzZVUXBvUzVpVzNodWIyZ2VZQmcAFkhnVTRsMTlNU3EtUURXeEt5amdlWlEAAAAAAAAAEYQWMlotLUltV1ZTVy1rWG9BcE5rdVh0UQABFjJZMzZVUXBvUzVpVzNodWIyZ2VZQmcAAA==")
    private String pit;

    @ApiModelProperty(name = "상품 검색 결과 리스트", notes = "상품 검색 결과 리스트")
    private List<ProductListItemResult> productResultList;
}
