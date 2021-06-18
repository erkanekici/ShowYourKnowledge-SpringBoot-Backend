package com.service;

import com.common.CommonConstants;
import com.common.ObjectConversionUtil;
import com.dao.UserInfoRepository;
import com.dto.UserInfoDTO;
import com.entity.UserInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

//    @Autowired
//    UserInfoRepository userInfoRepository;

    private final UserInfoRepository userInfoRepository;
    public UserServiceImpl(UserInfoRepository userInfoRepository){
        this.userInfoRepository = userInfoRepository;
    }

    @Transactional
    @Override
    public UserInfoDTO register(UserInfoDTO userInfoDTO) {
        //TODO kayıtlı email kontrolü yap
        UserInfo userInfo = ObjectConversionUtil.getInstance().convertObjectByObject(userInfoDTO, new TypeReference<UserInfo>(){}); //TODO map metoduna cevir
        userInfo.setUserName(userInfo.getEmail()); //TODO think
        userInfo = this.userInfoRepository.save(userInfo);

        // TODO send activation mail

        return ObjectConversionUtil.getInstance().convertObjectByObject(userInfo, new TypeReference<UserInfoDTO>() {});
    }

    @Override
    public UserInfoDTO getUserInfoByEmailAndPassword(String email, String password) {
        try {
            Optional<UserInfo> userInfo = userInfoRepository.findByEmailAndPassword(email, password);
            if (userInfo.isPresent()) {
                LOGGER.info("UserService - getUserInfoByEmailAndPassword - userInfo : {}", userInfo.get().toString());
                UserInfoDTO userInfoDTO = ObjectConversionUtil.getInstance().convertObjectByObject(userInfo.get(), new TypeReference<UserInfoDTO>() {
                });
                return userInfoDTO;
            } else {
                LOGGER.error("UserService ERROR - getUserInfoByEmailAndPassword - userInfo NULL");
                return null;
            }
        } catch (Exception e){
            //TODO nonuniqresult ex burada dönebilir
            LOGGER.error("UserService ERROR - getUserInfoByEmailAndPassword - Exception: {}",e.getMessage());
            return null;
        }
    }

    @Override
    public List<UserInfoDTO> getUserInfoByUserRegisterTimeIntervalAndActivity(String registerTimeInterval, int active) {
//        AtomicInteger index = new AtomicInteger(0);
//        index.incrementAndGet()
        return userInfoRepository.findAll().stream()
                .filter(item -> {
                    if (item.getActivity() == active) {
                        return true;
                    } else {
                        return false;
                    }
                })
                .filter(item -> {
                    String[] arrOfDateStr = getArrOfDateStr(registerTimeInterval);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CommonConstants.USERINFO_DATE_FORMATTER);
                    LocalDate startDate = LocalDate.parse(arrOfDateStr[0], formatter);
                    LocalDate endDate = LocalDate.parse(arrOfDateStr[1], formatter);
                    return (item.getCreatedTime().toLocalDate().isAfter(startDate) || item.getCreatedTime().toLocalDate().isEqual(startDate))
                            && item.getCreatedTime().toLocalDate().isBefore(endDate);
                })
                .map(item -> {
                    UserInfoDTO userInfoDTO = ObjectConversionUtil.getInstance().convertObjectByObject(item, new TypeReference<UserInfoDTO>(){});
                    return  userInfoDTO;
                }) //new UserInfoDTO(item))
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
