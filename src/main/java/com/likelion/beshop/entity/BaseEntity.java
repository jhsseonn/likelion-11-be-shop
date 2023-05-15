package com.likelion.beshop.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Negative;

@EntityListeners(value = {AuditingEntityListener.class}) //상속 받는 클래스에 추가해줘야하는 어노테이션
@MappedSuperclass //얘도
@Getter
@Setter
//다른 엔티티에서 상속받아 사용할 것이기 때문에 추상클래스로 설정
public abstract class BaseEntity extends BaseTimeEntity {

    @CreatedBy // 엔티티가 생성되어 저장될 때 자동으로 저장하도록 어노테이션 추가
    @Column(updatable = false) // 수정할 수 없도록 어노테이션 추가 //creat는 빼먹지 말고 해당 어노테이션 추가해주기
    private String createdBy;

    @LastModifiedBy //엔티티의 값을 변경할 때 시간?을 자동으로 저장하도록 어노테이션 추가
    private String updatedBy;
}

