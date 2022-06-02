package kr.polymarket.domain.user.repository;

import kr.polymarket.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u left join fetch u.userFile where u.id = :id")
    Optional<User> findWithUserFileById(Long id);

    Optional<User> findByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

}
