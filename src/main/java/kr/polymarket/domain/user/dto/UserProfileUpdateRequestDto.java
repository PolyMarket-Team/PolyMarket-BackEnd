package kr.polymarket.domain.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ApiModel(description = "사용자 프로필 수정 요청 모델")
public class UserProfileUpdateRequestDto {

    @ApiModelProperty(name = "사용자 별명", notes = "사용자 별명", example = "별명")
    private String nickname;

    @ApiModelProperty(name = "사용자 ID", notes = "사용자 ID", example = "99999")
    private Long profileImageFileId;

}
