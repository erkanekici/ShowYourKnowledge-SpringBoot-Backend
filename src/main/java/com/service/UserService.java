package com.service;

import com.dto.UserInfoDTO;

import java.util.List;

public interface UserService {
    UserInfoDTO register(UserInfoDTO userInfoDTO);
    UserInfoDTO getUserInfoByEmailAndPassword(String email, String password);
    List<UserInfoDTO> getUserInfoByUserRegisterTimeIntervalAndActivity(String registerTimeInterval, int active);
    boolean deneme();
}
