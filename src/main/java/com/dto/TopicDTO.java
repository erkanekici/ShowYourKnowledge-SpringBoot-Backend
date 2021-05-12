package com.dto;

import lombok.Data;

@Data
public class TopicDTO {

    private String id;
    private String userid1;
    private String userid2;

    public TopicDTO() {
    }

    public TopicDTO(String id, String userid1, String userid2) {
        this.id = id;
        this.userid1 = userid1;
        this.userid2 = userid2;
    }

//    @Override
//    public String toString() {
//        return "{\"id\":\"" + id + "\"," +
//                "\"userid1\":\"" + userid1 + "\"," +
//                "\"userid2\":\"" + userid2 + "\"," +
//                "}";
//    }

}
