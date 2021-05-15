package com.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserInfoDTO extends BaseDTO{

    private Long id;
    private String email;
    private String password;
    private String userName;
    private int activity;
    private int wrongEntry;

//    public UserInfoDTO(UserInfo userInfo){
//        this.setId(userInfo.getId());
//    }

}
