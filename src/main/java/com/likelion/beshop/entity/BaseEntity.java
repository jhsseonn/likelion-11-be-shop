package com.likelion.beshop.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

//다른 엔티티에서 상속받아 사용할 것이기 때문에 추상클래스로 설정
public abstract class BaseEntity extends BaseTimeEntity {

    @CreatedBy // 엔티티가 생성되어 저장될 때 자동으로 저장하도록 어노테이션 추가
    private String createdBy;

    @LastModifiedBy //엔티티의 값을 변경할 때 시간?을 자동으로 저장하도록 어노테이션 추가
    private String updatedBy;
}

