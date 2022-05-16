package kr.polymarket.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@SuperBuilder
@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class FileBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @Setter
    private boolean isDelete;

    @CreatedDate
    private LocalDateTime createDate;
}
