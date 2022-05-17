package com.loggerservice.processor;

import com.loggerservice.config.ApplicationData;
import com.loggerservice.dto.LogFileEventDto;
import com.loggerservice.entity.Event;
import com.loggerservice.enums.EventType;
import com.loggerservice.enums.State;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Slf4j
@Component
public class EventItemProcessor implements ItemProcessor<LogFileEventDto, Event> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ApplicationData applicationData;

    private final Map<String, LogFileEventDto> logFileEventMap = new ConcurrentHashMap<>();
    private final Map<String, Event> eventMap = new ConcurrentHashMap<>();

    @Override
    public Event process(final LogFileEventDto eventDto) throws Exception {

        if (logFileEventMap.containsKey(eventDto.getId())) {
            LogFileEventDto eventDto2 = logFileEventMap.get(eventDto.getId());
            long executionTime = getEventExecutionTime(eventDto, eventDto2);

            Event event = new Event();
            event.setId(eventDto.getId());
            event.setDuration(Math.toIntExact(executionTime));
            String host = eventDto2.getHost() != null ? eventDto2.getHost() : (eventDto.getHost() != null ? eventDto.getHost() : null);
            event.setHost(host);
            String type = eventDto2.getType() != null ? EventType.fromValue(eventDto2.getType()).getValue() : (eventDto.getType() != null ? EventType.fromValue(eventDto.getType()).getValue() : null);
            event.setType(type);
            event.setAlert(Boolean.FALSE);

            // if the execution time is more than the specified threshold, flag the alert as TRUE
            if (executionTime > applicationData.getAlertThresholdMs()) {
                event.setAlert(Boolean.TRUE);
                log.trace("!!! Execution time for the event {} is {}ms", eventDto.getId(), executionTime);
            }
            eventMap.put(eventDto.getId(), event);

            // remove from the temporary map as we found the matching event
            logFileEventMap.remove(eventDto.getId());
        } else {
            logFileEventMap.put(eventDto.getId(), eventDto);
        }
        return eventMap.get(eventDto.getId());
    }

    private long getEventExecutionTime(LogFileEventDto event1, LogFileEventDto event2) {
        LogFileEventDto endEvent = Stream.of(event1, event2).filter(e -> State.FINISHED.getValue().equals(e.getState())).findFirst().orElse(null);
        LogFileEventDto startEvent = Stream.of(event1, event2).filter(e -> State.STARTED.getValue().equals(e.getState())).findFirst().orElse(null);

        return Objects.requireNonNull(endEvent).getTimestamp() - Objects.requireNonNull(startEvent).getTimestamp();
    }
}
