package kr.polymarket.domain.user.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EmailCodeRequestDto {
    private String email;
    private String authCode;
}
