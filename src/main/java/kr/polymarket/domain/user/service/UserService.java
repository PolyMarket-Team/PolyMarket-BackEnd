package kr.polymarket.domain.user.service;

import kr.polymarket.domain.user.dto.SignUpDto;
import kr.polymarket.domain.user.entity.User;
import kr.polymarket.domain.user.exception.EmailAuthCodeAuthFailureException;
import kr.polymarket.domain.user.exception.UserEmailAlreadyExistsException;
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
    public void createUser(SignUpDto signUpDto) {
        //중복 여부 검증
        emailValidateDuplicated(signUpDto.getEmail());
        verifyValidateDuplicated(signUpDto.getEmail());
        nicknameValidateDuplication(signUpDto.getNickname());

        User user = userRepository.save(
                User.builder()
                        .email(signUpDto.getEmail())
                        .password(bCryptPasswordEncoder.encode(signUpDto.getPassword()))
                        .nickname(signUpDto.getNickname())
                        .createDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .build());
    }

    /**
     * 이메일 존재 여부 확인
     * @param email
     */
    public void emailValidateDuplicated(String email) {
        if (userRepository.findByEmail(email).isPresent())
            throw new UserEmailAlreadyExistsException();
    }

    /**
     * 닉네임 중복 확인 메서드
     */
    public void nicknameValidateDuplication(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
    }

    /**
     * 이메일 인증여부 확인 메서드
     */
    public void verifyValidateDuplicated(String email) {
        // TODO Null Point 체크, 앞단에서 유효성 검사
        if (emailRepository.findByEmail(email).get().getVerify() == Verify.EMAIL_CHECK_REQUIRE)
            throw new EmailAuthCodeAuthFailureException();
    }


}
