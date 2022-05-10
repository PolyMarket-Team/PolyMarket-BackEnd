package kr.polymarket.domain.user.util;

import kr.polymarket.global.config.AsyncConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * Email Util 테스트
 */
@SpringBootTest(classes = {AsyncConfig.class, MailSenderAutoConfiguration.class, JavaMailSender.class, EmailUtil.class})
@Slf4j
public class EmailUtilTest {

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private Executor threadPoolTaskExecutor;

    @Autowired
    private JavaMailSender mailSender;

    @Test
    void 인증코드_생성_테스트() {
        // given

        // when
        String authCode1Digits = emailUtil.createCode(1);
        String authCode6Digits = emailUtil.createCode(6);
        String authCode10000Digits = emailUtil.createCode(10000);

        // then
        // 코드가 의도한 길이 만큼 생성됐는지 검증
        assertThat(authCode1Digits.length()).isEqualTo(1);
        assertThat(authCode6Digits.length()).isEqualTo(6);
        assertThat(authCode10000Digits.length()).isEqualTo(10000);

        // 코드가 숫자로만 이루어진 문자열인지 검증
        assertThat(authCode1Digits.chars().allMatch(Character::isDigit)).isTrue();
        assertThat(authCode6Digits.chars().allMatch(Character::isDigit)).isTrue();
        assertThat(authCode10000Digits.chars().allMatch(Character::isDigit)).isTrue();
    }

    @Test
    @Description("구글 smtp 서버 사용시 발신제한으로 인해 테스트 실패" +
            "스레드 풀 기반의 비동기, 동시성 바탕의 이메일 단건씩 여러개 전송 테스트")
    void 이메일_여러건_전송_테스트() throws InterruptedException {
        // given
        final int EMAIL_SEND_NUM = 100;

        // when
        AtomicInteger completeNum = new AtomicInteger(0);
        for (int i = 0; i < EMAIL_SEND_NUM; i++) {
            threadPoolTaskExecutor.execute(() -> {
                emailUtil.send("polymarket.team@gmail.com", emailUtil.createCode(6));
                completeNum.incrementAndGet();
            });
        }

        // then
        await().atMost(Duration.ofSeconds(30))
                .until(() -> completeNum.get() == EMAIL_SEND_NUM);
    }

    @Test
    @Description("!!! 구글 smtp 서버 사용시 발신제한으로 인해 테스트 실패" +
            "스래드 풀 기반의 비동기, 동시성 바탕의 이메일 배치 전송 테스트")
    void 이메일_배치전송_테스트() {
        // given
        final int SEND_CHUNK = 10;
        final int SEND_TIMES = 10;
        final int EMAIL_SEND_NUM = SEND_CHUNK * SEND_TIMES;
        final String email = "polymarket.team@gmail.com";

        final SimpleMailMessage[] simpleMailMessageArr = IntStream.range(0, SEND_CHUNK)
                .boxed()
                .map(i -> {
                    SimpleMailMessage smm = new SimpleMailMessage();
                    smm.setTo(email);
                    smm.setSubject("회원가입 이메일 인증");
                    smm.setText(email + "의 인증코드는" + "0000" + "입니다");
                    return smm;
                })
                .toArray(SimpleMailMessage[]::new);

        // when
        AtomicInteger completeNum = new AtomicInteger(0);
        for(int i = 0; i < SEND_TIMES; i++) {
            threadPoolTaskExecutor.execute(() -> {
                mailSender.send(simpleMailMessageArr);
                completeNum.incrementAndGet();
            });
        }

        // then
        await().atMost(Duration.ofSeconds(30))
                .until(() -> completeNum.get() == EMAIL_SEND_NUM);
    }

}
