package com.service;

import com.common.ObjectConversionUtil;
import com.dao.UserTransactionsRepository;
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

    private final UserTransactionsRepository userTransactionsRepository;
    public UserTransactionServiceImpl(UserTransactionsRepository userTransactionsRepository){
        this.userTransactionsRepository = userTransactionsRepository;
    }

    @Transactional
    @Override
    public Long saveRequestLog(UserTransactionsDTO userTransactionsDTO) {
        UserTransactions userTransactions = ObjectConversionUtil.getInstance().convertObjectByObject(userTransactionsDTO, new TypeReference<UserTransactions>(){}); //TODO map metoduna cevir
        userTransactions = this.userTransactionsRepository.save(userTransactions);
        return userTransactions.getId();
    }

    @Transactional
    @Override
    public void saveResponseLog(Long id, Long userId, String response) {
        int updatedRows = userTransactionsRepository.setResponse(id, userId, response);
        if (updatedRows == 1) {
            LOGGER.info("UserTransactionServiceImpl - saveResponseLog - Record Updated > recordId: {} , userId: {} , response: {} ", id, userId, response);
        } else if (updatedRows == 0) {
            LOGGER.error("UserTransactionServiceImpl ERROR - saveResponseLog - No Record Updated > recordId: {} , userId: {} , response: {} ", id, userId, response);
        } else{
            LOGGER.error("UserTransactionServiceImpl ERROR - saveResponseLog - Multi Record Updated > recordId: {} , userId: {} , response: {} ", id, userId, response);
        }
    }

    @Override
    public boolean isValidTransactionId(String transactionId, Long userId) {
        List<UserTransactions> userTransactions = userTransactionsRepository.findByTransactionIdAndUserId(transactionId, userId);
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
