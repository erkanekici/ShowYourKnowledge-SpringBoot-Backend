package com.service;

import com.common.ObjectConversionUtil;
import com.controller.constants.ApiConstants;
import com.dao.UserTransactionRepository;
import com.dto.UserTransactionsDTO;
import com.entity.UserTransactions;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class UserTransactionServiceImpl implements UserTransactionService{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserTransactionServiceImpl.class);
    private static final long timeoutMinute = 12;

//    @Autowired
//    UserTransactionsRepository userTransactionsRepository;

    private final UserTransactionRepository userTransactionRepository;
    public UserTransactionServiceImpl(UserTransactionRepository userTransactionRepository){
        this.userTransactionRepository = userTransactionRepository;
    }

    @Transactional
    @Override
    public Long saveRequestLog(UserTransactionsDTO userTransactionsDTO) {
        UserTransactions userTransactions = ObjectConversionUtil.getInstance().convertObjectByObject(userTransactionsDTO, new TypeReference<UserTransactions>(){}); //TODO map metoduna cevir
        userTransactions = this.userTransactionRepository.save(userTransactions);
        return userTransactions.getId();
    }

    @Transactional
    @Override
    public void saveSuccessfulResponseLog(Long id, Long userId, String response) {
        int updatedRows = userTransactionRepository.setSuccessfulResponse(id, userId, response, ApiConstants.SUCCESSFUL_CODE);
        if (updatedRows == 1) {
            LOGGER.info("UserTransactionServiceImpl - saveSuccessfulResponseLog - Record Updated > recordId: {} , userId: {} , response: {} ", id, userId, response);
        } else if (updatedRows == 0) {
            LOGGER.error("UserTransactionServiceImpl ERROR - saveSuccessfulResponseLog - No Record Updated > recordId: {} , userId: {} , response: {} ", id, userId, response);
        } else{
            LOGGER.error("UserTransactionServiceImpl ERROR - saveSuccessfulResponseLog - Multi Record Updated > recordId: {} , userId: {} , response: {} ", id, userId, response);
        }
    }

    @Transactional
    @Override
    public void saveFailedResponseLog(Long id, Long userId, String response, String errorCode, String errorMessage) {
        int updatedRows = userTransactionRepository.setFailedResponse(id, userId, response, ApiConstants.FAILED_CODE, errorCode, errorMessage);
        if (updatedRows == 1) {
            LOGGER.info("UserTransactionServiceImpl - saveFailedResponseLog - Record Updated > recordId: {} , userId: {} , response: {} ", id, userId, response);
        } else if (updatedRows == 0) {
            LOGGER.error("UserTransactionServiceImpl ERROR - saveFailedResponseLog - No Record Updated > recordId: {} , userId: {} , response: {} ", id, userId, response);
        } else{
            LOGGER.error("UserTransactionServiceImpl ERROR - saveFailedResponseLog - Multi Record Updated > recordId: {} , userId: {} , response: {} ", id, userId, response);
        }
    }

    @Override
    public boolean isValidTransactionId(String transactionId, Long userId) {
        List<UserTransactions> userTransactions = userTransactionRepository.findByTransactionIdAndUserId(transactionId, userId);
        if (!userTransactions.isEmpty()) {
            return userTransactions.stream()
                    .map(UserTransactions::getCreatedTime)
                    .min(OffsetDateTime::compareTo)
                    .get()
                    .plusMinutes(timeoutMinute)
                    .isAfter(OffsetDateTime.now());

            //get oldest date userTransaction record:
            //UserTransactions userTransactions = Collections.min(userTransactions, Comparator.comparing(UserTransactions::getCreatedTime));
        } else {
            return false;
        }
    }
}
