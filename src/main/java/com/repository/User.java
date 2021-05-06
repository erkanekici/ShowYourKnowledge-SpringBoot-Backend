package com.repository;

import java.time.OffsetDateTime;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getWrongEntry() {
        return wrongEntry;
    }

    public void setWrongEntry(int wrongEntry) {
        this.wrongEntry = wrongEntry;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public OffsetDateTime getDbCreatedDateTime() {
        return dbCreatedDateTime;
    }

    public void setDbCreatedDateTime(OffsetDateTime dbCreatedDateTime) {
        this.dbCreatedDateTime = dbCreatedDateTime;
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\"," +
                "\"email\":\"" + email + "\"," +
                "\"password\":\"" + password + "\"," +
                "\"username\":\"" + username + "\"," +
                "\"active\":\"" + active + "\"," +
                "\"wrongEntry\":\"" + wrongEntry + "\"," +
                "\"registerTime\":\"" + registerTime + "\"," +
                "}";
    }

}
