package kr.polymarket.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ApiModel(description = "인증 토큰 응답 모델")
public class TokenResponseDto {

    @ApiModelProperty(name = "access token",
            notes = "인증이 필요한 API 요청시 Header(X-AUTH-TOKEN) 에 첨부해서 보내는 인증용 jwt access token",
            example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGVtYWlsLmNvbSIsImlhdCI6MTY1MjM2MDc0NCwiZXhwIjoxNjUyMzYyNTQ0fQ.N7zfQD4qVOPkcA1Iy3_QZrMXmxB-pwyy9ciIcx2u7EEBBKTHD2VYizmU01conO8L37CVuLwPYAhoNqmcjBXmLg")
    private String accessToken;

    @ApiModelProperty(name = "refresh token",
            notes = "access token 갱신을 위해 사용하는 token, POST /users/token/refresh with Header(X-REFRESH-TOKEN)으로 요청할떄 필요", example
            = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGVtYWlsLmNvbSIsImlhdCI6MTY1MjM2Mjg4NiwiZXhwIjoxNjUyOTY3Njg2fQ.vIkbIyND5A8ULrMwK08_SCXRMTJD8ErllQNNS9AeWLp6-ZRd_7HkV-vUYrGAjWLIp6W8VamB9_B9G7XM-9sN")
    private String refreshToken;


    @ApiModelProperty(name = "access token 만료일",
            notes = "access token 만료일", example = "2022-03-30 12:30:32")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime accessTokenExpiryDateTime;

    @ApiModelProperty(name = "refresh token 만료일",
            notes = "refresh token 만료일", example = "2022-03-30 12:30:32")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime refreshTokenExpiryDateTime;
}
