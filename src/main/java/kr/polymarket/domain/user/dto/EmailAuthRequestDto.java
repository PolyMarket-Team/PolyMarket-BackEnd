package kr.polymarket.domain.user.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.polymarket.domain.user.entity.EmailAuth;
import kr.polymarket.domain.user.entity.Verify;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "이메일 인증코드 전송 요청 모델")
public class EmailAuthRequestDto {

    @NotBlank(message = "이메일이 빈칸입니다.")
    @Email(message = "이메일 형식이 맞지 않습니다")
    @ApiModelProperty(name = "이메일", notes = "인증 코드를 보낼 이메일 주소", required = true, example = "example@email.com")
    private String email;

    public EmailAuth createEmailAuth(EmailAuthRequestDto emailAuthRequestDto){
        return EmailAuth.builder()
                .email(emailAuthRequestDto.getEmail())
                .verify(Verify.EMAIL_CHECK_REQUIRE)
                .build();
    }
}


