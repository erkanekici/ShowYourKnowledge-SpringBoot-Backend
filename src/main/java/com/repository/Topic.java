package com.repository;

public class Topic {

    private String id;
    private String userid1;
    private String userid2;

    public Topic() {
    }

    public Topic(String id, String userid1, String userid2) {
        this.id = id;
        this.userid1 = userid1;
        this.userid2 = userid2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid1() {
        return userid1;
    }

    public void setUserid1(String userid1) {
        this.userid1 = userid1;
    }

    public String getUserid2() {
        return userid2;
    }

    public void setUserid2(String userid2) {
        this.userid2 = userid2;
    }



    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\"," +
                "\"userid1\":\"" + userid1 + "\"," +
                "\"userid2\":\"" + userid2 + "\"," +
                "}";
    }

}
