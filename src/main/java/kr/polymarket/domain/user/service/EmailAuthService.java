package kr.polymarket.domain.user.service;

import kr.polymarket.domain.user.dto.EmailAuthDto;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailAuthService {

    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final RedisService redisService;
    private final RedisTemplate redisTemplate;
    private final EmailUtil emailUtil;

    /**
     * 이메일 전송 및 임시 저장
     */
    @Transactional
    public void sendEmail(EmailAuthDto emailAuthDto) {
        String authCode = emailUtil.createCode(6);
        signValidateDuplicated(emailAuthDto.getEmail());

        //이메일 인증까지 완료 했지만 회원가입을 완료하지 않은 사람들 검증
        if (emailRepository.existsByEmail(emailAuthDto.getEmail()) != false) {
            redisService.setDataWithExpiration(RedisKey.CODE.getKey() + emailAuthDto.getEmail(), authCode, 60 * 5L);
            emailUtil.send(emailAuthDto.getEmail(), authCode);
        }
        emailValidateDuplicated(emailAuthDto.getEmail());

        //처음 인증받는 사람들 email DB저장 및 인증코드 전송
        EmailAuth emailAuth = emailAuthDto.createEmailAuth(emailAuthDto);
        emailRepository.save(emailAuth);
        redisService.setDataWithExpiration(RedisKey.CODE.getKey() + emailAuthDto.getEmail(), authCode, 60 * 5L);
        emailUtil.send(emailAuthDto.getEmail(), authCode);
    }

    /**
     * 이메일 인증 완료
     */
    @Transactional
    public void confirmEmail(EmailCodeRequestDto emailCodeRequestDto) {

        Object object = redisTemplate.opsForValue().get(emailCodeRequestDto.getEmail());

        //인증코드가 만료되거나 인증코드 값이 같지 않으면 에러발생
        if (object == null || !emailCodeRequestDto.getAuthCode().equals(object.toString())) {
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
    public void signValidateDuplicated(String email) {
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

