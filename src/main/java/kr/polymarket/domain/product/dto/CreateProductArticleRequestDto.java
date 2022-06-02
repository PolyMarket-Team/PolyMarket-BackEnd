package kr.polymarket.domain.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import kr.polymarket.domain.product.entity.Product;
import kr.polymarket.domain.user.dto.UserProfileResponse;
import kr.polymarket.domain.user.entity.User;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "게시글 생성")
@Builder
public class CreateProductArticleRequestDto {

    @ApiModelProperty(name = "글 제목", notes = "상품 글 제목")
    @NotBlank(message = "글 제목을 입력해주세요.")
    @Length(min = 2, max = 100, message = "글 제목은 2~100자까지 가능합니다.")
    private String title;

    @ApiModelProperty(name = "상품설명 내용", notes = "상품 설명 내용")
    @NotBlank(message = "글 내용을 입력해주세요.")
    @Length(min = 20, max = 500, message = "상품설명 내용은 20~500자까지 입력 가능합니다. ")
    private String content;

    @ApiModelProperty(name = "상품가격", notes = "상품가격")
    private Integer price;

    @ApiParam(value = "상품 카테고리", example = "HOME_APPLIANCES")
    @NotNull(message = "카테고리를 선택해주세요.")
    private ProductCategory productCategory;

    @ApiModelProperty(name = "상품 이미지 파일 아이디")
    private List<Long> fileIdList;

}

