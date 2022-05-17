package com.loggerservice.service;

import com.loggerservice.dto.EventDto;
import com.loggerservice.entity.Event;
import com.loggerservice.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<EventDto> getAllProcessedEvents(){
        log.info("fetching all processed events.....");
        final List<EventDto> eventDtos = new ArrayList<>();
        Iterable<Event> all = eventRepository.findAll();
        all.forEach(event->{
            EventDto eventDto = modelMapper.map(event, EventDto.class);
            eventDtos.add(eventDto);
            log.info(String.valueOf(eventDto));
        });
        return eventDtos;
    }

    public EventDto getProcessedEventById(final String eventId){
        log.info("fetching processed event by eventId : {} ", eventId);
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        EventDto eventDto = optionalEvent.isPresent() ? modelMapper.map(optionalEvent.get(), EventDto.class) : null;
        log.info(eventDto.toString());
        return eventDto;
    }
    
}
