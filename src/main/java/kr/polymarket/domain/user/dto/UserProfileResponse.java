package kr.polymarket.domain.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.polymarket.domain.user.entity.User;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ApiModel(description = "사용자 프로필 응답 모델")
public class UserProfileResponse {

    @ApiModelProperty(name = "사용자 ID", notes = "사용자 ID", example = "99999")
    private Long userId;

    @ApiModelProperty(name = "사용자 별명", notes = "사용자 별명", example = "별명")
    private String nickname;

    @ApiModelProperty(name = "프로필 이미지 URL",
            notes = "사용자 프로필 이미지 URL",
            example = "https://dnvefa72aowie.cloudfront.net/origin/profile/202108/E2132D4F6E3627C64060741B8B1D398ADF38A3ECE7EB235CB13E7A36361DC95B.jpg")
    private String profileImageUrl;


    public static UserProfileResponse of(User user) {
        return UserProfileResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImageUrl(user.getUserFile() == null ? null : user.getUserFile().getFileUrl())
                .build();
    }

}
