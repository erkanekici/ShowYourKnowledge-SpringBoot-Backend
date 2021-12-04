package com.dto;

import lombok.*;
import lombok.experimental.FieldNameConstants;

@Data
@EqualsAndHashCode(callSuper = false)
@FieldNameConstants
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SampleRequestDTO {

    private String name;

}
