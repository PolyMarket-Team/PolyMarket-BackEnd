package kr.polymarket.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "이메일 인증코드 확인 요청 모델")
public class EmailAuthCheckRequestDto {

    @NotBlank(message = "이메일이 빈칸입니다.")
    @Email(message = "이메일 형식이 맞지 않습니다")
    @ApiModelProperty(name = "이메일", notes = "인증할 이메일", required = true, example = "example@email.com")
    private String email;

    @NotBlank(message = "인증코드가 빈칸입니다.")
    @Length(min = 6, max = 6, message = "인증코드는 6자리 입니다.")
    @ApiModelProperty(name = "인증코드", notes = "인증할 이메일의 인증코드", required = true, example = "123456")
    private String authCode;
}
