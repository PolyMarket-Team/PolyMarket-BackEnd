package kr.polymarket.domain.user.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailUtil {

    private final JavaMailSender javaMailSender;

    //이메일 전송
    @Async("threadPoolTaskExecutor")
    public void send(String email, String authCode) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(email);
        smm.setSubject("회원가입 이메일 인증");
        smm.setText(email + "의 인증코드는" + authCode + "입니다");

        javaMailSender.send(smm);
    }

    public String createCode(int length) {
        StringBuffer key = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            key.append((random.nextInt(10)));
        }
        return key.toString();
    }
}