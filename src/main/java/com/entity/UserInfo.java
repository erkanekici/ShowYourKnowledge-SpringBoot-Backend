package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_info", indexes = {
        @Index(columnList = "email", name = "email_index"),
        @Index(columnList = "user_password", name = "user_password_index"),
        @Index(columnList = "user_name", name = "user_name_index"),
})
public class UserInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(name = "email")
    @NotNull
    private String email;

    @Column(name = "user_password")
    @NotNull
    private String password;

    @Column(name = "user_name")
    @NotNull
    private String userName;

    @Column(name = "activity")
    @NotNull
    private int activity; // 0:aktif değil - 1:aktif - 2:blokeli

    @Column(name = "wrong_entry")
    @NotNull
    private int wrongEntry;

}
