package kr.polymarket.domain.user.service;

import kr.polymarket.domain.user.dto.SignUpRequestDto;
import kr.polymarket.domain.user.dto.UserProfileResponse;
import kr.polymarket.domain.user.dto.UserProfileUpdateRequestDto;
import kr.polymarket.domain.user.entity.User;
import kr.polymarket.domain.user.entity.UserFile;
import kr.polymarket.domain.user.exception.*;
import kr.polymarket.domain.user.repository.EmailRepository;
import kr.polymarket.domain.user.repository.UserFileRepository;
import kr.polymarket.domain.user.repository.UserRepository;
import kr.polymarket.domain.user.entity.Verify;
import kr.polymarket.global.error.ErrorCode;
import kr.polymarket.global.error.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserFileRepository userFileRepository;
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
     * 유저 프로필 정보 조회
     */
    public UserProfileResponse findUserProfile(long userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> { throw new UserNotFoundException("존재하지않는 회원입니다."); });

        return UserProfileResponse.of(findUser);
    }

    /**
     * 사용자 프로필 수정
     */
    @Transactional
    public UserProfileResponse updateUserProfile(long userId, UserDetails userDetails, UserProfileUpdateRequestDto userProfileUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> { throw new UserNotFoundException("존재하지않는 회원입니다."); });

        if(!user.getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException("수정권한이 없는 사용자 입니다.");
        }

        UserFile userFile = userFileRepository.findByFileIdAndAndIsDelete(userProfileUpdateRequest.getProfileImageFileId(), false)
                .orElseThrow(() -> { throw new UserFileNotFoundException(ErrorCode.FILE_NOT_FOUND); });

        // 사용자 프로필을 교체하는 경우
        if(!userFile.equals(user.getUserFile())) {
            if(!ObjectUtils.isEmpty(userFile.getUser()) && !Objects.equals(userFile.getUser().getId(), user.getId())) {
                throw new ForbiddenException(String.format("파일에 대한 접근권한이 없습니다. userId: %d, updatedProfileFileId: %d", user.getId(), userFile.getFileId()));
            }

            UserFile previousUserProfileFile = user.getUserFile();
            previousUserProfileFile.setUser(null);
            previousUserProfileFile.setDelete(true);

            user.setUserFile(userFile);
            userFile.setUser(user);
        }

        user.setNickname(userProfileUpdateRequest.getNickname());

        return UserProfileResponse.of(user);
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
