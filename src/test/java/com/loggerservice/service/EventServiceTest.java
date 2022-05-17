package com.loggerservice.service;

import com.loggerservice.dto.EventDto;
import com.loggerservice.entity.Event;
import com.loggerservice.repository.EventRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private EventService eventService;

    @Test
    void getProcessedEventByEventIdTest(){

        Event event = new Event("eventId-2", 2, null, null, Boolean.FALSE);

        Mockito.when(eventRepository.findById(Mockito.anyString())).thenReturn(Optional.of(event));

        EventDto result = eventService.getProcessedEventById("eventId-2");

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo("eventId-2");
        assertThat(result.getDuration()).isEqualTo(2);
        assertThat(result.getType()).isNull();
        assertThat(result.getHost()).isNull();
        assertThat(result.getAlert()).isEqualTo(Boolean.FALSE);
    }
}
