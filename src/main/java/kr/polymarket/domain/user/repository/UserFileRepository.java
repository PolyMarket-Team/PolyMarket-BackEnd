package kr.polymarket.domain.user.repository;

import kr.polymarket.domain.user.entity.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFileRepository extends JpaRepository<UserFile, Long> {

    Optional<UserFile> findByFileIdAndAndIsDelete(long userId, boolean isDelete);
}
