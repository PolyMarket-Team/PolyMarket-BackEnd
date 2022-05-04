package kr.polymarket.domain.user.dto;


import com.sun.istack.NotNull;
import kr.polymarket.domain.user.entity.EmailAuth;
import kr.polymarket.domain.user.entity.Verify;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Email;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailAuthDto {

    @NotNull
    @Email(message = "이메일 형식이 맞지 않습니다")
    @Column(unique = true)
    private String email;

    public EmailAuth createEmailAuth(EmailAuthDto emailAuthDto){
        return EmailAuth.builder()
                .email(emailAuthDto.getEmail())
                .verify(Verify.EMAIL_CHECK_REQUIRE)
                .build();
    }
}


