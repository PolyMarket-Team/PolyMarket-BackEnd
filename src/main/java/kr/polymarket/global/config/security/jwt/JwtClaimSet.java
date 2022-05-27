package kr.polymarket.global.config.security.jwt;

import io.jsonwebtoken.Claims;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class JwtClaimSet {

    private String token;

    private Claims claims;
}
