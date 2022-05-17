package com.loggerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LogFileEventDto {

    private String id;

    private String state;

    private String type;

    private String host;

    private Long timestamp;
}
