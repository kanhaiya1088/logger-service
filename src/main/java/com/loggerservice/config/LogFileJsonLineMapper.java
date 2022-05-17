package com.loggerservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loggerservice.dto.LogFileEventDto;
import org.springframework.batch.item.file.LineMapper;

public class LogFileJsonLineMapper implements LineMapper<LogFileEventDto> {

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Interpret the line as a Json object and create a LogFileEventDto from it
     * @param line
     * @param lineNumber
     * @return
     * @throws Exception
     */
    @Override
    public LogFileEventDto mapLine(String line, int lineNumber) throws Exception {
        return mapper.readValue(line, LogFileEventDto.class);
    }
}
