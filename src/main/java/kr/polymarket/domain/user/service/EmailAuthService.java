package kr.polymarket.domain.user.service;

import kr.polymarket.domain.user.dto.EmailAuthDto;
import kr.polymarket.domain.user.dto.EmailAuthResultDto;
import kr.polymarket.domain.user.dto.EmailCodeRequestDto;
import kr.polymarket.domain.user.entity.EmailAuth;
import kr.polymarket.domain.user.entity.RedisKey;
import kr.polymarket.domain.user.exception.EmailAlreadySendException;
import kr.polymarket.domain.user.exception.EmailAuthCodeNotFoundException;
import kr.polymarket.domain.user.exception.EmailNotFoundException;
import kr.polymarket.domain.user.exception.UserEmailAlreadyExistsException;
import kr.polymarket.domain.user.repository.EmailRepository;
import kr.polymarket.domain.user.repository.UserRepository;
import kr.polymarket.domain.user.util.EmailUtil;
import kr.polymarket.global.properties.AppProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RedisService redisService;
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailUtil emailUtil;
    private final AppProperty appProperty;

    private static final long EMAIL_AUTH_EXPIRE_TIME = Duration.ofMinutes(5).toSeconds();
    private static final String SERVER_STANDARD_TIMEZONE = "Asia/Seoul";
    private static final int EMAIL_AUTH_CODE_LENGTH = 6;

    /**
     * 이메일 전송 및 임시 저장
     */
    public EmailAuthResultDto sendAuthCodeToEmail(EmailAuthDto emailAuthDto) {
        String authCode = emailUtil.createCode(EMAIL_AUTH_CODE_LENGTH);
        validateSignUpDuplicated(emailAuthDto.getEmail());

        LocalDateTime expiredDateTime = ZonedDateTime.now(ZoneId.of(SERVER_STANDARD_TIMEZONE)).plus(5, ChronoUnit.MINUTES).toLocalDateTime();

        EmailAuthResultDto emailAuthResult = EmailAuthResultDto.builder()
                .email(emailAuthDto.getEmail())
                .authCode("prod".equals(appProperty.getEnv())? null: authCode)
                .expireDateTime(expiredDateTime)
                .expireDateTimeZone(SERVER_STANDARD_TIMEZONE)
                .build();

        //이메일 인증까지 완료 했지만 회원가입을 완료하지 않은 사람들 검증
        if (emailRepository.existsByEmail(emailAuthDto.getEmail())) {
            // TODO 회원가입은 안됐으나 이메일 인증코드를 보낸적이 있는 경우 이메일 재전송까지 시간제한을 걸지 여부 기획

            redisService.setDataWithExpiration(RedisKey.CODE.getKey() + emailAuthDto.getEmail(), authCode, EMAIL_AUTH_EXPIRE_TIME);
            emailUtil.send(emailAuthDto.getEmail(), authCode);
            return emailAuthResult;
        }

        //처음 인증받는 사람들 email DB저장 및 인증코드 전송
        EmailAuth emailAuth = emailAuthDto.createEmailAuth(emailAuthDto);
        emailRepository.save(emailAuth);
        redisService.setDataWithExpiration(RedisKey.CODE.getKey() + emailAuthDto.getEmail(), authCode, EMAIL_AUTH_EXPIRE_TIME);
        emailUtil.send(emailAuthDto.getEmail(), authCode);
        return emailAuthResult;
    }

    /**
     * 이메일 인증 완료
     */
    public void confirmEmailAuthCode(EmailCodeRequestDto emailCodeRequestDto) {

        String emailAuthCode = redisTemplate.opsForValue().get(emailCodeRequestDto.getEmail());

        //인증코드가 만료되거나 인증코드 값이 같지 않으면 에러발생
        if (!emailCodeRequestDto.getAuthCode().equals(emailAuthCode)) {
            throw new EmailAuthCodeNotFoundException();
        }

        //인증이 완료되면 레디스에 저장된 정보를 삭제하고 이메일 상태변경
        EmailAuth emailAuth = emailRepository.findByEmail(emailCodeRequestDto.getEmail()).orElseThrow(EmailNotFoundException::new);
        redisService.deleteData(RedisKey.CODE.getKey() + emailCodeRequestDto.getEmail());
        emailAuth.emailVerifiedSuccess();
    }

    /**
     * 회원가입이 되어있는지 확인하는 메서드
     */
    public void validateSignUpDuplicated(String email) {
        if (userRepository.findByEmail(email).isPresent())
            throw new UserEmailAlreadyExistsException();
    }

    /**
     * 이메일 db에 이메일 저장되어 있는지 확인
     */
    public void emailValidateDuplicated(String email) {
        if (emailRepository.findByEmail(email).isPresent())
            throw new EmailAlreadySendException();
    }
}

