package com.likelion.beshop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@EntityListeners(value = {AuditingEntityListener.class})    //Auditing 적용 어노테이션
@MappedSuperclass   //공통 매핑 정보가 필요할 때 사용 -> 부모 클래스를 상속받는 자식 클래스에 매핑 정보만 제공
@Getter
@Setter
public abstract class BaseTimeEntity { // 다른 엔티티에서 상속받아 사용할 것이므로 추상(abstract)클래스로 설정
    // 등록시간
    @CreatedDate // Entitiy가 최초로 생성되어 저장될 때 시간이 자동으로 저장
    @Column(updatable = false) // 수정할 수 없도록 어노테이션 추가
    private LocalDateTime regTime;

    //수정 시간
    @LastModifiedDate // 값을 변경할 때 시간이 자동으로 저장
    private LocalDateTime updateTime;

}
