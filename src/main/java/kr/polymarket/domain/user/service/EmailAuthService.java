package kr.polymarket.domain.user.service;

import kr.polymarket.domain.user.dto.EmailAuthRequestDto;
import kr.polymarket.domain.user.dto.EmailAuthResultDto;
import kr.polymarket.domain.user.dto.EmailCodeRequestDto;
import kr.polymarket.domain.user.entity.EmailAuth;
import kr.polymarket.domain.user.entity.RedisKey;
import kr.polymarket.domain.user.exception.EmailAlreadySendException;
import kr.polymarket.domain.user.exception.EmailAuthCodeAuthFailureException;
import kr.polymarket.domain.user.exception.EmailNotFoundException;
import kr.polymarket.domain.user.exception.UserAlreadySignUpException;
import kr.polymarket.domain.user.repository.EmailRepository;
import kr.polymarket.domain.user.repository.RedisRepository;
import kr.polymarket.domain.user.repository.UserRepository;
import kr.polymarket.domain.user.util.EmailUtil;
import kr.polymarket.global.properties.AppProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailAuthService {

    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final RedisRepository redisRepository;
    private final EmailUtil emailUtil;
    private final AppProperty appProperty;

    private static final long EMAIL_AUTH_EXPIRE_TIME = Duration.ofMinutes(5).toSeconds();
    private static final String SERVER_STANDARD_TIMEZONE = "Asia/Seoul";
    private static final int EMAIL_AUTH_CODE_LENGTH = 6;

    /**
     * 이메일 전송 및 임시 저장
     */
    public EmailAuthResultDto sendAuthCodeToEmail(EmailAuthRequestDto emailAuthRequestDto) {
        String authCode = emailUtil.createCode(EMAIL_AUTH_CODE_LENGTH);
        validateSignUpDuplicated(emailAuthRequestDto.getEmail());

        LocalDateTime expiredDateTime = ZonedDateTime.now(ZoneId.of(SERVER_STANDARD_TIMEZONE)).plus(5, ChronoUnit.MINUTES).toLocalDateTime();

        EmailAuthResultDto emailAuthResult = EmailAuthResultDto.builder()
                .email(emailAuthRequestDto.getEmail())
                .authCode("prod".equals(appProperty.getEnv())? null: authCode)
                .expireDateTime(expiredDateTime)
                .expireDateTimeZone(SERVER_STANDARD_TIMEZONE)
                .build();

        //이메일 인증까지 완료 했지만 회원가입을 완료하지 않은 사람들 검증
        if (emailRepository.existsByEmail(emailAuthRequestDto.getEmail())) {
            // TODO 회원가입은 안됐으나 이메일 인증코드를 보낸적이 있는 경우 이메일 재전송까지 시간제한을 걸지 여부 기획

            redisRepository.setDataWithExpiration(RedisKey.EMAIL_AUTH_CODE.getKey() + emailAuthRequestDto.getEmail(), authCode, EMAIL_AUTH_EXPIRE_TIME);
            emailUtil.send(emailAuthRequestDto.getEmail(), authCode);
            return emailAuthResult;
        }

        //처음 인증받는 사람들 email DB저장 및 인증코드 전송
        EmailAuth emailAuth = emailAuthRequestDto.createEmailAuth(emailAuthRequestDto);
        emailRepository.save(emailAuth);
        redisRepository.setDataWithExpiration(RedisKey.EMAIL_AUTH_CODE.getKey() + emailAuthRequestDto.getEmail(), authCode, EMAIL_AUTH_EXPIRE_TIME);
        emailUtil.send(emailAuthRequestDto.getEmail(), authCode);
        return emailAuthResult;
    }

    /**
     * 이메일 인증 완료
     */
    public void confirmEmailAuthCode(EmailCodeRequestDto emailCodeRequestDto) {

        String emailAuthCode = redisRepository.getData(emailCodeRequestDto.getEmail());

        //인증코드가 만료되거나 인증코드 값이 같지 않으면 에러발생
        if (!emailCodeRequestDto.getAuthCode().equals(emailAuthCode)) {
            throw new EmailAuthCodeAuthFailureException();
        }

        //인증이 완료되면 레디스에 저장된 정보를 삭제하고 이메일 상태변경
        EmailAuth emailAuth = emailRepository.findByEmail(emailCodeRequestDto.getEmail()).orElseThrow(EmailNotFoundException::new);
        redisRepository.deleteData(RedisKey.EMAIL_AUTH_CODE.getKey() + emailCodeRequestDto.getEmail());
        emailAuth.emailVerifiedSuccess();
    }

    /**
     * 회원가입이 되어있는지 확인하는 메서드
     */
    public void validateSignUpDuplicated(String email) {
        if (userRepository.findByEmail(email).isPresent())
            throw new UserAlreadySignUpException("이미 가입된 회원으로 인증 메일을 보낼 수 없습니다.");
    }

    /**
     * 이메일 db에 이메일 저장되어 있는지 확인
     */
    public void emailValidateDuplicated(String email) {
        if (emailRepository.findByEmail(email).isPresent())
            throw new EmailAlreadySendException();
    }
}

