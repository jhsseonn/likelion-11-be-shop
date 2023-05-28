package com.likelion.beshop.entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
@Setter
// 다른 엔티티에서 상속받아 사용할 것이기 때문에 추상클래스로 설정
public abstract class BaseTimeEntity {

    @CreatedDate // 엔티티가 생성되어 저장될 때 시간을 자동으로 저장하도록 어노테이션 추가
    @Column(updatable = false) // 등록 시간은 수정할 수 없도록 어노테이션 추가
    private LocalDateTime regTime; // 등록 시간

    @LastModifiedDate // 엔티티의 값을 변경할 때 시간을 자동으로 저장하도록 어노테이션 추가
    private LocalDateTime updateTime; // 수정 시간
}

