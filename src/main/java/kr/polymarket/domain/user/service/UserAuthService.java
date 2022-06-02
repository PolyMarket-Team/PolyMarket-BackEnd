package kr.polymarket.domain.user.service;

import kr.polymarket.domain.user.dto.LoginRequestDto;
import kr.polymarket.domain.user.dto.LoginResponseDto;
import kr.polymarket.domain.user.dto.TokenResponseDto;
import kr.polymarket.domain.user.entity.User;
import kr.polymarket.domain.user.exception.InvalidRefreshTokenException;
import kr.polymarket.domain.user.exception.SignInFailureException;
import kr.polymarket.domain.user.exception.UserNotFoundException;
import kr.polymarket.domain.user.repository.RedisKeyPrefix;
import kr.polymarket.domain.user.repository.RedisRepository;
import kr.polymarket.domain.user.repository.UserRepository;
import kr.polymarket.global.config.security.jwt.JwtClaimSet;
import kr.polymarket.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisRepository redisService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 일반 로그인
     * param UserLoginRequestDto
     * @retrun
     */
    @Transactional
    public LoginResponseDto userSignIn(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(SignInFailureException::new);
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword()))
            throw new SignInFailureException();

        // access token 생성
        JwtClaimSet accessTokenSet = jwtTokenProvider.createToken(loginRequestDto.getEmail());

        // refresh token 생성 및 저장
        JwtClaimSet refreshTokenSet = jwtTokenProvider.createRefreshToken(loginRequestDto.getEmail());
        redisService.setDataWithExpiration(RedisKeyPrefix.REFRESH.buildKey(user.getEmail()), refreshTokenSet.getToken(),
                JwtTokenProvider.REFRESH_TOKEN_VALID_TIME);

        return LoginResponseDto.builder()
                .userId(user.getId())
                .accessToken(accessTokenSet.getToken())
                .refreshToken(refreshTokenSet.getToken())
                .accessTokenExpiryDateTime(accessTokenSet.getClaims().getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .refreshTokenExpiryDateTime(refreshTokenSet.getClaims().getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
    }

    /**
     * 토큰 재발급
     * @param refreshToken
     */
    @Transactional
    public TokenResponseDto tokenRefresh(String refreshToken) {
        String email = jwtTokenProvider.getUserEmail(refreshToken);
        String findRefreshToken = redisService.getData(RedisKeyPrefix.REFRESH.buildKey(email));
        if (findRefreshToken == null || !findRefreshToken.equals(refreshToken))
            throw new InvalidRefreshTokenException();

        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        // 새로운 access token 발급
        JwtClaimSet newAccessTokenSet = jwtTokenProvider.createToken(user.getEmail());

        // 새로운 refresh token 발급 및 저장
        JwtClaimSet newRefreshTokenSet = jwtTokenProvider.createRefreshToken(user.getEmail());
        redisService.setDataWithExpiration(RedisKeyPrefix.REFRESH.buildKey(user.getEmail()),
                refreshToken, JwtTokenProvider.REFRESH_TOKEN_VALID_TIME);

        return TokenResponseDto.builder()
                .accessToken(newAccessTokenSet.getToken())
                .refreshToken(newRefreshTokenSet.getToken())
                .accessTokenExpiryDateTime(newAccessTokenSet.getClaims().getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .refreshTokenExpiryDateTime(newRefreshTokenSet.getClaims().getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
    }
}

