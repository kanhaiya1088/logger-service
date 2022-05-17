package com.loggerservice.controller;

import com.loggerservice.dto.EventDto;
import com.loggerservice.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("event")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping(value = "/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EventDto getProcessedEventById(@PathVariable final String eventId){
        log.info("------- Event Fetched by EventId Successfully---------");
        return eventService.getProcessedEventById(eventId);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EventDto> getAllProcessedEvent(){
        log.info("------- All Processed Events Fetched Successfully---------");
        return eventService.getAllProcessedEvents();
    }
}
