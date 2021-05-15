package com.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {

    @Column(name = "deleted")
    @NotNull
    private Boolean deleted;

    @Column(name = "created_time")
    @NotNull
    private OffsetDateTime createdTime;

    @Column(name = "updated_time")
    private OffsetDateTime updatedTime;

    @PrePersist
    void onCreate() {
        this.setCreatedTime(OffsetDateTime.now());
        this.setDeleted(false);
    }

    @PreUpdate
    void onPersist() {
        this.setUpdatedTime(OffsetDateTime.now());
    }
}
