package kr.polymarket.domain.user.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_file")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Setter
    private User user;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @Setter
    private boolean isDelete;

    @CreatedDate
    private LocalDateTime createDate;

}
