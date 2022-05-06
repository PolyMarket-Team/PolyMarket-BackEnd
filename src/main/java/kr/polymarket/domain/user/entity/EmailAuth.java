package kr.polymarket.domain.user.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(name = "email")
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class EmailAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email()
    private String email;

    @Enumerated(EnumType.STRING)
    private Verify verify;


    public void emailVerifiedSuccess() {
        this.verify = Verify.EMAIL_CHECK_COMPLETE;
    }
}
