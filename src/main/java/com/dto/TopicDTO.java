package com.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TopicDTO extends BaseDTO {

    private String id;
    private Long userId1;
    private Long userId2;

}
