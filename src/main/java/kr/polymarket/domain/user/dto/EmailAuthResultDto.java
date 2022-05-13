package kr.polymarket.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailAuthResultDto {

    @ApiModelProperty(name = "이메일", notes = "이메일", example = "example@email.com")
    private String email;

    @ApiModelProperty(name = "이메일 인증코드", notes = "이메일 인증코드(실서버에서는 응답x)", example = "123456")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String authCode;

    @ApiModelProperty(name = "이메일 인증코드 만료시각", notes = "이메일 인증코드 만료시각 (유효기간 5분)", example = "2022-01-01 12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireDateTime;

    @ApiModelProperty(name = "이메일 인증코드 만료시각 타임존", notes = "이메일 인증코드 만료시각 타임존", example = "Asia/Seoul")
    private String expireDateTimeZone;
}
