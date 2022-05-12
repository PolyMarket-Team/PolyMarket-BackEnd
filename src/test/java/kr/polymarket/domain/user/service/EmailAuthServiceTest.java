package kr.polymarket.domain.user.service;

import kr.polymarket.domain.user.dto.EmailAuthDto;
import kr.polymarket.domain.user.dto.EmailAuthResultDto;
import kr.polymarket.domain.user.entity.User;
import kr.polymarket.domain.user.exception.UserEmailAlreadyExistsException;
import kr.polymarket.domain.user.repository.EmailRepository;
import kr.polymarket.domain.user.repository.UserRepository;
import kr.polymarket.domain.user.util.EmailUtil;
import kr.polymarket.global.properties.AppProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
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
    @Description("회원가입된 이메일은 아니나 이메일인증코드를 보낸적이 있는 이메일로 인증코드를 보내는 경우 테스트")
    void sendAuthCodeToEmail_회원가입x_이메일인증코드를_보낸적o() {
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

    @Test
    @Description("회원가입된 이메일은 아니나 이메일인증코드를 보낸적이 없는 이메일로 인증코드를 보내는 경우 테스트")
    void sendAuthCodeToEmail_회원가입x_이메일인증코드를_보낸적x() {
        // given
        final String email = "test@email.com";
        final EmailAuthDto emailAuthDto = EmailAuthDto.builder()
                .email(email)
                .build();

        given(userRepository.findByEmail(email)).willReturn(Optional.empty());
        given(emailRepository.existsByEmail(email)).willReturn(false);
        given(appProperty.getEnv()).willReturn("dev");
        given(emailUtil.createCode(anyInt())).willReturn("123456");

        // when
        EmailAuthResultDto emailAuthResult = emailAuthService.sendAuthCodeToEmail(emailAuthDto);

        // then
        assertThat(emailAuthResult.getEmail()).isEqualTo(email);
        assertThat(emailAuthResult.getAuthCode().length()).isEqualTo(6);
    }

    @Test
    @Description("회원가입된 이메일로 인증코드를 요청하는 경우 테스트")
    void sendAuthCodeToEmail_회원가입o() {
        // given
        final String email = "test@email.com";
        final EmailAuthDto emailAuthDto = EmailAuthDto.builder()
                .email(email)
                .build();

        given(userRepository.findByEmail(email)).willReturn(Optional.of(User.builder().build()));

        // when
        Throwable exception = catchThrowable(() -> emailAuthService.sendAuthCodeToEmail(emailAuthDto));

        // then
        assertThat(exception)
                .isInstanceOf(UserEmailAlreadyExistsException.class);
    }

    @Test
    @Description("실서버에서 인증코드 노출x 테스트")
    void sendAuthCodeToEmail_반환값_응답코드_노출x() {
        // given
        final String email = "test@email.com";
        final EmailAuthDto emailAuthDto = EmailAuthDto.builder()
                .email(email)
                .build();

        given(userRepository.findByEmail(email)).willReturn(Optional.empty());
        given(emailRepository.existsByEmail(email)).willReturn(false);
        given(appProperty.getEnv()).willReturn("prod");
        given(emailUtil.createCode(anyInt())).willReturn("123456");

        // when
        EmailAuthResultDto emailAuthResult = emailAuthService.sendAuthCodeToEmail(emailAuthDto);

        // then
        assertThat(emailAuthResult.getEmail()).isEqualTo(email);
        assertThat(emailAuthResult.getAuthCode()).isNull();
    }
}
