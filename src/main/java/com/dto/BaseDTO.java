package com.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
public class BaseDTO {

    private Boolean deleted;
    private OffsetDateTime createdTime;
    private OffsetDateTime updatedTime;

}
