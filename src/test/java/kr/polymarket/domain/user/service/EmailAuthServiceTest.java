package kr.polymarket.domain.user.service;

import kr.polymarket.domain.user.dto.EmailAuthDto;
import kr.polymarket.domain.user.dto.EmailAuthResultDto;
import kr.polymarket.domain.user.repository.EmailRepository;
import kr.polymarket.domain.user.repository.UserRepository;
import kr.polymarket.domain.user.util.EmailUtil;
import kr.polymarket.global.properties.AppProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class EmailAuthServiceTest {

    @InjectMocks
    private EmailAuthService emailAuthService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private RedisService redisService;

    @Mock
    private EmailUtil emailUtil;

    @Mock
    private AppProperty appProperty;

    @Test
    void sendAuthCodeToEmail() {
        // given
        final String email = "test@email.com";
        final EmailAuthDto emailAuthDto = EmailAuthDto.builder()
                .email(email)
                .build();

        given(userRepository.findByEmail(email)).willReturn(Optional.empty());
        given(emailRepository.existsByEmail(email)).willReturn(true);
        given(appProperty.getEnv()).willReturn("dev");
        given(emailUtil.createCode(anyInt())).willReturn("123456");

        // when
        EmailAuthResultDto emailAuthResult = emailAuthService.sendAuthCodeToEmail(emailAuthDto);

        // then
        assertThat(emailAuthResult.getEmail()).isEqualTo(email);
        assertThat(emailAuthResult.getAuthCode().length()).isEqualTo(6);
    }

}
