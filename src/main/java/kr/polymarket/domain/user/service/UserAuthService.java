package kr.polymarket.domain.user.service;

import kr.polymarket.domain.user.dto.LoginRequestDto;
import kr.polymarket.domain.user.dto.LoginResponseDto;
import kr.polymarket.domain.user.dto.TokenResponseDto;
import kr.polymarket.domain.user.repository.RedisKeyPrefix;
import kr.polymarket.domain.user.entity.User;
import kr.polymarket.domain.user.exception.InvalidRefreshTokenException;
import kr.polymarket.domain.user.exception.SignInFailureException;
import kr.polymarket.domain.user.exception.UserNotFoundException;
import kr.polymarket.domain.user.repository.RedisRepository;
import kr.polymarket.domain.user.repository.UserRepository;
import kr.polymarket.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        String refreshToken = jwtTokenProvider.createRefreshToken(loginRequestDto.getEmail());
        redisService.setDataWithExpiration(RedisKeyPrefix.REFRESH.buildKey(user.getEmail()), refreshToken,
                JwtTokenProvider.REFRESH_TOKEN_VALID_TIME);
        return LoginResponseDto.builder()
                .userId(user.getId())
                .accessToken(jwtTokenProvider.createToken(loginRequestDto.getEmail()))
                .refreshToken(refreshToken)
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
        String newAccessToken = jwtTokenProvider.createToken(user.getEmail());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());
        redisService.setDataWithExpiration(RedisKeyPrefix.REFRESH.buildKey(user.getEmail()),
                refreshToken, JwtTokenProvider.REFRESH_TOKEN_VALID_TIME);

        return new TokenResponseDto(newAccessToken, newRefreshToken);
    }
}

