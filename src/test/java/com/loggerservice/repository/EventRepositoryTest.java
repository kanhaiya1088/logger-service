package com.loggerservice.repository;

import com.loggerservice.entity.Event;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventRepositoryTest {
    
    @Autowired
    private EventRepository eventRepository;
    
    @Test
    public void whenFindingProcessedEventById_thenCorrect() {
        eventRepository.save(new Event("eventId-1", 3, "APPLICATION_LOG", "12345", Boolean.FALSE));
        assertThat(eventRepository.findById("eventId-1")).isInstanceOf(Optional.class);
    }
    
    @Test
    public void whenFindingAllProcessedEvents_thenCorrect() {
        eventRepository.save(new Event("eventId-2", 3, "APPLICATION_LOG", "12345", Boolean.FALSE));
        eventRepository.save(new Event("eventId-1", 4, null, null, Boolean.TRUE));
        Iterable<Event> all = eventRepository.findAll();
        assertThat(eventRepository.findAll()).isInstanceOf(List.class);
    }

    @Test
    public void whenSavingEvents_thenCorrect() {
        eventRepository.save(new Event("eventId", 3, "APPLICATION_LOG", "12345", Boolean.FALSE));
        Event event = eventRepository.findById("eventId").orElseGet(null);
        assertThat(event.getDuration()).isEqualTo(3);
        assertThat(event.getType()).isEqualTo("APPLICATION_LOG");
        assertThat(event.getHost()).isEqualTo("12345");
        assertThat(event.getAlert()).isEqualTo(Boolean.FALSE);
    }
}