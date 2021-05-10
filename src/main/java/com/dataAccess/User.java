package com.dataAccess;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class User {

    private String id;
    private String email;
    private String password;
    private String username;
    private int active;
    private int wrongEntry;
    private String registerTime;
    private OffsetDateTime dbCreatedDateTime;

    public User() {
    }

    public User(String id, String email, String password, String username,int active,int wrongEntry, String registerTime,OffsetDateTime dbCreatedDateTime) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.active = active;
        this.wrongEntry = wrongEntry;
        this.registerTime = registerTime;
        this.dbCreatedDateTime = dbCreatedDateTime;
    }

//    @Override
//    public String toString() {
//        return "{\"id\":\"" + id + "\"," +
//                "\"email\":\"" + email + "\"," +
//                "\"password\":\"" + password + "\"," +
//                "\"username\":\"" + username + "\"," +
//                "\"active\":\"" + active + "\"," +
//                "\"wrongEntry\":\"" + wrongEntry + "\"," +
//                "\"registerTime\":\"" + registerTime + "\"," +
//                "}";
//    }

}
