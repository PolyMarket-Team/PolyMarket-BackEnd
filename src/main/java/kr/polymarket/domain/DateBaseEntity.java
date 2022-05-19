package kr.polymarket.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@SuperBuilder
@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class DateBaseEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false, name = "create_date")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(nullable = false, name = "update_date")
    private LocalDateTime updateDate;

}
