package kr.polymarket.domain.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "로그인 응답 모델")
public class LoginResponseDto {

    @ApiModelProperty(name = "사용자 ID", notes = "사용자 식별자", example = "1")
    private Long id;

    @ApiModelProperty(name = "access token",
            notes = "인증이 필요한 API 요청시 Header(X-AUTH-TOKEN) 에 첨부해서 보내는 인증용 jwt access token",
            example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGVtYWlsLmNvbSIsImlhdCI6MTY1MjM2MDc0NCwiZXhwIjoxNjUyMzYyNTQ0fQ.N7zfQD4qVOPkcA1Iy3_QZrMXmxB-pwyy9ciIcx2u7EEBBKTHD2VYizmU01conO8L37CVuLwPYAhoNqmcjBXmLg")
    private String accessToken;

    @ApiModelProperty(name = "refresh token",
            notes = "사용자 식별자", example
            = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGVtYWlsLmNvbSIsImlhdCI6MTY1MjM2Mjg4NiwiZXhwIjoxNjUyOTY3Njg2fQ.vIkbIyND5A8ULrMwK08_SCXRMTJD8ErllQNNS9AeWLp6-ZRd_7HkV-vUYrGAjWLIp6W8VamB9_B9G7XM-9sN")
    private String refreshToken;
}
