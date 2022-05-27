package kr.polymarket.domain.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.util.List;

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
    private List<ProductCategory> categoryList;

    @Range(min = 0, max = 20, message = "페이지 번호는 0 이상 20 이하이어야 합니다.")
    @ApiParam(value = "페이지(최대 20페이지까지 지원), 디폴트 값: 0", example = "2")
    private Integer page;

    @ApiParam(value = "PIT(Point In Time): 검색 시점 기준 값, 검색 페이징을 위한 스냅샷 아이디 @@ 주의: 첫번째 페이지 요청시에는 빈값으로 요청",
            example = "39K1AwEScHJvZHVjdF9zZWFyY2hfaWR4FjJZMzZVUXBvUzVpVzNodWIyZ2VZQmcAFkhnVTRsMTlNU3EtUURXeEt5amdlWlEAAAAAAAAAEYQWMlotLUltV1ZTVy1rWG9BcE5rdVh0UQABFjJZMzZVUXBvUzVpVzNodWIyZ2VZQmcAAA==")
    private String pit;

    @Getter
    @RequiredArgsConstructor
    public enum ProductCategory {
        USED_CAR("C01", "중고차", 1),
        DIGITAL_DEVICE("C01", "디지털기기", 2),
        HOME_APPLIANCES("C02", "생활가전", 3),
        FURNITURE_INTERIOR("C03", "가구/인테리어", 4),
        INFANT_CHILD("C04", "유아동", 5),
        LIFE_PROCESSED_FOOD("C05", "생활가전", 6),
        CHILDREN_BOOKS("C06", "유아도서", 7),
        SPORTS_LEISURE("C07", "스포츠/레저", 8),
        WOMEN_ACCESSORIES("C08", "여성잡화", 9),
        WOMEN_CLOTHING("C09", "여성의류", 10),
        MENS_FASHION_ACCESSORIES("C10", "남성패션/잡화", 11),
        GAME_HOBBY("C11", "게임/취미", 12),
        BEAUTY("C12", "뷰티/미용", 13),
        PET_SUPPLIES("C13", "반려동물용품", 14),
        BOOK_TICKET_ALBUM("C14", "도서/티켓/음반", 15),
        PLANT("C15", "식물", 16),
        ETC("C16", "기타중고물품", 17)
        ;

        private final String code;
        private final String value;
        private final Integer id;

    }
}