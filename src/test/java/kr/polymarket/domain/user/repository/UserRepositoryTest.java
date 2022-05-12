package kr.polymarket.domain.user.repository;

import kr.polymarket.domain.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private String setUpEmail;

    @BeforeEach
    void setup() {
        setUpEmail = "test1@test.com";
        String encodedPassword = ")12@*(sjb902!92kxcNw32JK&#3$asd&42f";
        String nickname = "testNick1";

        userRepository.save(
                User.builder()
                        .email(setUpEmail)
                        .password(encodedPassword)
                        .nickname(nickname)
                        .createDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .build());
    }


    @Test
    void 사용자등록_테스트() {
        // given
        String email = "test2@test.com";
        String encodedPassword = "S2D@1!I5(#3$&4f*sd89m72@8f89d%0_~/23..as234@4";
        String nickname = "testNick2";

        // when
        User savedUser = userRepository.save(
                User.builder()
                        .email(email)
                        .password(encodedPassword)
                        .nickname(nickname)
                        .createDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .build());

        // then
        User user = userRepository.getById(savedUser.getId());
        assertThat(user).isSameAs(savedUser);
    }

    @Test
    void 사용자이메일조회_존재하는경우_테스트() {
        final boolean existByEmail = userRepository.existsByEmail(setUpEmail);
        assertThat(existByEmail).isTrue();
    }

    @Test
    void 사용자이메일조회_존재하지않는경우_테스트() {
        final boolean existByEmail = userRepository.existsByEmail("no@test.com");
        assertThat(existByEmail).isFalse();
    }
}
