package kr.polymarket.domain.user.entity;


import kr.polymarket.domain.DateBaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;


@Entity
@Getter
@Setter
@SuperBuilder
@Table(name = "`user`")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends DateBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 300, nullable = false)
    private String password;

    @Column(length = 10, nullable = false)
    private String nickname;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private UserFile userFile;

}

