package com.loggerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loggerservice.dto.EventDto;
import com.loggerservice.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    @Test
    void getProcessedEventByIdTest() throws Exception {
        EventDto eventDto = new EventDto("eventId-2", 2, null, null, Boolean.FALSE);
        Mockito.when(eventService.getProcessedEventById(Mockito.anyString())).thenReturn(eventDto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/event/eventId-2")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        JSONAssert.assertEquals(new ObjectMapper().writeValueAsString(eventDto),mvcResult.getResponse().getContentAsString(), false);

        Mockito.verify(eventService, Mockito.times(1)).getProcessedEventById(eventDto.getId());
    }
}
