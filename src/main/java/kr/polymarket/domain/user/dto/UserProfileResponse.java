package kr.polymarket.domain.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserProfileResponse {

    @ApiModelProperty(name = "이메일 인증코드", notes = "이메일 인증코드(실서버에서는 응답x)", example = "123456")
    private Long userId;

    @ApiModelProperty(name = "이메일 인증코드", notes = "이메일 인증코드(실서버에서는 응답x)", example = "123456")
    private String nickname;

    @ApiModelProperty(name = "이메일 인증코드", notes = "이메일 인증코드(실서버에서는 응답x)", example = "123456")
    private String profileImageUrl;

}
