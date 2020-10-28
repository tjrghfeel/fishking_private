package com.tobe.fishking.v2.entity;


import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTime {

    // Entity가 생성되어 저장될 때 시간이 자동 저장됩니다.
    @CreatedDate
    @Column(updatable = false, columnDefinition = "datetime  comment '생성일시'  ")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(columnDefinition = "datetime  comment '수정일시'  ")
    private LocalDateTime modifiedDate;

}