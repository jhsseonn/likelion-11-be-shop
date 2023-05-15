package com.likelion.beshop.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@EntityListeners(value = {AuditingEntityListener.class})    //Auditing 적용 어노테이션
@MappedSuperclass   //공통 매핑 정보가 필요할 때 사용 -> 부모 클래스를 상속받는 자식 클래스에 매핑 정보만 제공
@Getter
public abstract class BaseEntity extends BaseTimeEntity{ // BaseTimeEntity에서 등록 시간과 수정 시간을 상속받고 등록자와 수정자 정보를 저장하는 엔티티, 다른 엔티티에서 상속받아 사용하니까 추상클래스(abstract)

    // 등록자
    @CreatedBy // Entitiy가 최초로 생성되어 저장될 때 자동으로 저장
    @Column(updatable=false)
    public String createdBy;

    //수정자
    @LastModifiedBy // 값을 변경할 때 자동으로 저장
    private String modifiedBy;
}
