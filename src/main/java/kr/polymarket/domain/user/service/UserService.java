package kr.polymarket.domain.user.service;

import kr.polymarket.domain.user.dto.SignUpRequestDto;
import kr.polymarket.domain.user.entity.User;
import kr.polymarket.domain.user.exception.EmailAuthCodeAuthFailureException;
import kr.polymarket.domain.user.exception.EmailNotFoundException;
import kr.polymarket.domain.user.exception.NicknameAlreadyExistsException;
import kr.polymarket.domain.user.exception.UserAlreadySignUpException;
import kr.polymarket.domain.user.repository.EmailRepository;
import kr.polymarket.domain.user.repository.UserRepository;
import kr.polymarket.domain.user.entity.Verify;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public void createUser(SignUpRequestDto signUpDto) {
        //중복 여부, 이메일 인증체크 검증
        validateSignUpDuplicationBy(signUpDto.getEmail());
        validateEmailAuthCheckBy(signUpDto.getEmail());
        validateNicknameDuplicationBy(signUpDto.getNickname());

        userRepository.save(
                User.builder()
                        .email(signUpDto.getEmail())
                        .password(bCryptPasswordEncoder.encode(signUpDto.getPassword()))
                        .nickname(signUpDto.getNickname())
                        .createDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .build());
    }

    /**
     * 회원가입 여부 확인
     * @param email
     */
    public void validateSignUpDuplicationBy(String email) {
        if (userRepository.findByEmail(email).isPresent())
            throw new UserAlreadySignUpException();
    }

    /**
     * 닉네임 중복 확인 메서드
     */
    public void validateNicknameDuplicationBy(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new NicknameAlreadyExistsException("이미 존재하는 닉네임입니다.");
        }
    }

    /**
     * 이메일 인증여부 확인 메서드
     */
    public void validateEmailAuthCheckBy(String email) {
        if (emailRepository.findByEmail(email).orElseThrow(EmailNotFoundException::new)
                .getVerify() == Verify.EMAIL_CHECK_REQUIRE)
            throw new EmailAuthCodeAuthFailureException("이메일 인증이 되지않았습니다.");
    }


}
