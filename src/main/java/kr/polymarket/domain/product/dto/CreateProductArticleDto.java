package kr.polymarket.domain.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "게시글 생성")
@Builder
public class CreateProductArticleDto {

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

    @ApiModelProperty(name = "카테고리 아이디", notes = "카테고리 아이디")
    @NotNull(message = "카테고리 아이디를 입력해주세요.")
    private Integer categoryId;

    @ApiModelProperty(name = "상품 이미지 파일 아이디")
    private List<Long> fileIdList;

}