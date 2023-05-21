package com.likelion.beshop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
@EntityListeners(value={AuditingEntityListener.class})
@MappedSuperclass
@Setter
@Getter
public abstract class BaseTimeEntity {
    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime regTime;
    @LastModifiedDate
    private LocalDateTime updateTime;
}
