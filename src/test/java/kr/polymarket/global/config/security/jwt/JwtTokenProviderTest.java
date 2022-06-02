package kr.polymarket.global.config.security.jwt;


import kr.polymarket.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserDetailService userDetailService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        // TODO jwtTokenProvider 내부에 @Value 필드가 있는데 추후 property로 분리후 아래 코드 제거
        ReflectionTestUtils.setField(jwtTokenProvider,
                "secretKey",
                "asdbf9uSd92389*(@!jx231!@3(");
    }

    @Test
    void access_token_생성_및_파싱_테스트() {
        // given
        final String email = "test@email.com";
        JwtClaimSet accessToken = jwtTokenProvider.createToken(email);

        // when
        String parsedEmail = jwtTokenProvider.getUserEmail(accessToken.getToken());

        // then
        assertThat(parsedEmail).isEqualTo(email);
    }

    @Test
    void refresh_token_생성_테스트() {
        // given
        final String email = "test@email.com";
        JwtClaimSet refreshToken = jwtTokenProvider.createRefreshToken(email);

        // when
        String parsedEmail = jwtTokenProvider.getUserEmail(refreshToken.getToken());

        // then
        assertThat(parsedEmail).isEqualTo(email);
        System.out.println(refreshToken);
    }
}
