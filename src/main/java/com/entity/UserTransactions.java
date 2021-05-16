package com.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_transactions", indexes = {
        @Index(columnList = "user_id", name = "user_id_index"),
        @Index(columnList = "transaction_id", name = "transaction_id_index"),
})
public class UserTransactions extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(name = "transaction_id")
    @NotNull
    private String transactionId;

    @Column(name = "user_id")
    @NotNull
    private Long userId;

    @Column(name = "service_name")
    @NotNull
    private String serviceName;

    @Column(name = "method_name")
    @NotNull
    private String methodName;

    @Column(name = "request")
    private String request;

    @Column(name = "response")
    private String response;

    @Column(name = "result_code")
    private int resultCode;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "client_ip")
    private String clientIp;

}
