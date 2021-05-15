package com.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity
@Table(name = "topic")
public class Topic extends BaseEntity{

    @Id
    @NotNull
    private String id;

    @Column(name = "user_id_1")
    private Long userId1;

    @Column(name = "user_id_2")
    private Long userId2;
}
