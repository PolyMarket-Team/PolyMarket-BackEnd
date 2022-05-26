package kr.polymarket.global.config.security.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${spring.jwt.secretKey}")
    private String secretKey;

    private final UserDetailService userDetailService;

    public static final long TOKEN_VALID_TIME = 30 * 60 * 1000L; // 30분
    public static final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 7; // 7일
    public static final String ACCESS_TOKEN_HEADER_NAME = "X-AUTH-TOKEN";


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public static String getAccessTokenHeaderName() {
        return ACCESS_TOKEN_HEADER_NAME;
    }

    // 토큰 키는 중복되지않는 값인 email로 지정, H512알고리즘 적용, 토큰유표시간 설정(발급순간부터 30분)
    public JwtClaimSet createToken(String email) {
        Date now = new Date();
        Claims claims = Jwts.claims()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TOKEN_VALID_TIME));

        return JwtClaimSet.builder()
                .token(Jwts.builder()
                        .setClaims(claims)
                        .signWith(SignatureAlgorithm.HS512, secretKey)
                        .compact()
                )
                .claims(claims)
                .build();
    }

    public JwtClaimSet createRefreshToken(String email) {
        Date now = new Date();
        Claims claims = Jwts.claims()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME));

        return JwtClaimSet.builder()
                .token(Jwts.builder()
                        .setClaims(claims)
                        .signWith(SignatureAlgorithm.HS512, secretKey)
                        .compact()
                )
                .claims(claims)
                .build();
    }


    //토큰을 통해 인증 객체를 얻는다
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailService.loadUserByUsername(getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    //이메일을 얻기 위해 토큰 디코딩 지정된 키를 통해 해석
    public String getUserEmail(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    //토큰 이용을 위해 Header에서 꺼내옴
    public String resolveToken(HttpServletRequest req) {
        return req.getHeader(ACCESS_TOKEN_HEADER_NAME);
    }


    //토큰이 만료되었는지 검증하는 메소드(토큰 디코딩 후 현재시간과 비교해 확인)
    public boolean validateTokenExceptExpiration(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}



