package com.dao;

import com.entity.UserTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTransactionsRepository extends JpaRepository<UserTransactions, Long> {

    Optional<UserTransactions> findById(String id);

    List<UserTransactions> findByUserId(Long userId);

    List<UserTransactions> findByTransactionIdAndUserId(String transactionId, Long userId);

    @Modifying
    @Query("update UserTransactions u set u.userId = :userId, u.response = :response where u.id = :id")
    int setResponse(@Param("id") Long id, @Param("userId") Long userId, @Param("response") String response); //TODO return type int
}
