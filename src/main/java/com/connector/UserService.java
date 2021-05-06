package com.connector;

import com.model.UserConstants;
import com.repository.DBUtil;
import com.repository.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    @Autowired
    DBUtil dbUtil;

    private List<User> getUsersByUserRegisterTimeIntervalAndActivity(String registerTimeInterval, int active) {
//        AtomicInteger index = new AtomicInteger(0);
//        index.incrementAndGet()
        return dbUtil.getAllUsers().stream()
                .filter(item -> {
                    if (item.getActive() == active) {
                        return true;
                    } else {
                        return false;
                    }
                })
                .filter(item -> {
                    String[] arrOfDateStr = getArrOfDateStr(registerTimeInterval);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(UserConstants.DATE_FORMATTER);
                    LocalDate startDate = LocalDate.parse(arrOfDateStr[0], formatter);
                    LocalDate endDate = LocalDate.parse(arrOfDateStr[1], formatter);
                    return (item.getDbCreatedDateTime().toLocalDate().isAfter(startDate) || item.getDbCreatedDateTime().toLocalDate().isEqual(startDate))
                            && item.getDbCreatedDateTime().toLocalDate().isBefore(endDate);
                })
                //.map(item -> new FundComplianceRulesLogDTO(item))
                .collect(Collectors.toList());
    }

    private String[] getArrOfDateStr(String registerTime) {
        String date = registerTime;
        if (date.charAt(0) == ',') {
            date = date.substring(1);
        }
        return date.split(",");
    }
}
