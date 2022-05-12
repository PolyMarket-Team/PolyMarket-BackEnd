package kr.polymarket.domain.user.dto;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.NotNull;
import kr.polymarket.domain.user.entity.EmailAuth;
import kr.polymarket.domain.user.entity.Verify;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailAuthRequestDto {

    @NotBlank(message = "이메일이 빈칸입니다.")
    @Email(message = "이메일 형식이 맞지 않습니다")
    private String email;

    public EmailAuth createEmailAuth(EmailAuthRequestDto emailAuthRequestDto){
        return EmailAuth.builder()
                .email(emailAuthRequestDto.getEmail())
                .verify(Verify.EMAIL_CHECK_REQUIRE)
                .build();
    }
}


