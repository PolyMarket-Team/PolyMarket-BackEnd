package kr.polymarket.domain.user.repository;

import kr.polymarket.domain.user.entity.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFileRepository extends JpaRepository<UserFile, Long> {

    @Query("select uf from UserFile uf where uf.fileId = :fileId and uf.isDelete = :isDelete and uf.user.id is null")
    Optional<UserFile> findByFileId(long fileId, boolean isDelete);
}
