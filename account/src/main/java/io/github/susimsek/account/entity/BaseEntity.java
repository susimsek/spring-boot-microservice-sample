package io.github.susimsek.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @ToString
public class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @CreatedBy
    @Column(name = "created_by",nullable = false, updatable = false, length = 20)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private Instant updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by",insertable = false, length = 20)
    private String updatedBy;
}