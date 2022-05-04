package kr.polymarket.domain.user.service;

import kr.polymarket.domain.user.dto.LoginRequestDto;
import kr.polymarket.domain.user.dto.LoginResponseDto;
import kr.polymarket.domain.user.dto.TokenRequestDto;
import kr.polymarket.domain.user.dto.TokenResponseDto;
import kr.polymarket.domain.user.entity.RedisKey;
import kr.polymarket.domain.user.exception.InvalidRefreshTokenException;
import kr.polymarket.domain.user.exception.LoginFailureException;
import kr.polymarket.domain.user.exception.UserNotFoundException;
import kr.polymarket.domain.user.entity.User;
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
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 일반 로그인
     * param UserLoginRequestDto
     * @retrun
     */
    @Transactional
    public LoginResponseDto userSignIn(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(LoginFailureException::new);
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPasssword()))
            throw new LoginFailureException();

        String refreshToken = jwtTokenProvider.createRefreshToken();
        redisService.setDataWithExpiration(RedisKey.REFRESH.getKey() + user.getEmail(), refreshToken, JwtTokenProvider.REFRESH_TOKEN_VALID_TIME);
        return new LoginResponseDto(user.getId(), jwtTokenProvider.createToken(loginRequestDto.getEmail()), refreshToken);
    }

    /**
     * 토큰 재발급
     * @param requestDto
     */
    @Transactional
    public TokenResponseDto tokenRefresh(TokenRequestDto requestDto) {
        String findRefreshToken = redisService.getData(RedisKey.REFRESH.getKey() + requestDto.getEmail());
        if (findRefreshToken == null || !findRefreshToken.equals(requestDto.getRefreshToken()))
            throw new InvalidRefreshTokenException();

        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(UserNotFoundException::new);
        String accessToken = jwtTokenProvider.createToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken();
        redisService.setDataWithExpiration(RedisKey.REFRESH.getKey() + user.getEmail(), refreshToken, JwtTokenProvider.REFRESH_TOKEN_VALID_TIME);

        return new TokenResponseDto(accessToken, refreshToken);
    }
}

