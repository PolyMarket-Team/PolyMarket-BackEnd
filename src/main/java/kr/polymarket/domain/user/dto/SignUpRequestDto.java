package kr.polymarket.domain.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ApiModel(description = "회원가입 요청 모델")
public class SignUpRequestDto {

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "이메일 형식이 맞지 않습니다")
    @ApiModelProperty(name = "이메일", notes = "이메일", required = true, example = "example@email.com")
    private String email;

    @Pattern(regexp ="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$",
            message = "비밀번호는 특수문자, 영어, 숫자가 모두 포함되어야합니다.")
    @Length(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    @ApiModelProperty(name = "패스워드", notes = "패스워드(특수문자, 영어, 숫자가 모두 포함)", required = true, example = "q1w2e3r4!@#")
    private String password;

    @Length(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하여야 합니다.")
    @ApiModelProperty(name = "별명", notes = "별명(2~10자)", required = true, example = "별명")
    private String nickname;

}
