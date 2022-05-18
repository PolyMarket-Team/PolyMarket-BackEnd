package kr.polymarket.domain.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ApiModel(description = "사용자 프로필 수정 요청 모델")
public class UserProfileUpdateRequestDto {

    @ApiModelProperty(name = "사용자 별명", notes = "사용자 별명", example = "별명")
    @Length(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하여야 합니다.")
    @NotBlank(message = "별명을 입력해주세요")
    private String nickname;

    @ApiModelProperty(name = "사용자 ID", notes = "사용자 ID", example = "99999")
    @NotNull(message = "프로필 이미지 파일아이디를 반드시 입력해야합니다.")
    @Range(min = 1, message = "파일아이디는 반드시 1이상이어야합니다.")
    private Long profileImageFileId;

}
