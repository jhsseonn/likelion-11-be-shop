package com.likelion.beshop.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;

public abstract class BaseEntity {
    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime legTime;
    @LastModifiedDate
    private LocalDateTime updateTime;
}
