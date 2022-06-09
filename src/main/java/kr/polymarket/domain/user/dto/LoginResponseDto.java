package kr.polymarket.domain.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@ApiModel(description = "로그인 응답 모델")
public class LoginResponseDto extends TokenResponseDto {

    @ApiModelProperty(name = "사용자 프로필 정보", notes = "사용자 프로필 정보")
    private UserProfileResponse userProfile;

}
