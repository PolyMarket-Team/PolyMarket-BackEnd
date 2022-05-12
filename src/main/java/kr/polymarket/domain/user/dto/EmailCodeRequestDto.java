package kr.polymarket.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailCodeRequestDto {

    @NotBlank(message = "이메일이 빈칸입니다.")
    @Email(message = "이메일 형식이 맞지 않습니다")
    private String email;

    @NotBlank(message = "인증코드가 빈칸입니다.")
    @Length(min = 6, max = 6, message = "인증코드는 6자리 입니다.")
    private String authCode;
}
