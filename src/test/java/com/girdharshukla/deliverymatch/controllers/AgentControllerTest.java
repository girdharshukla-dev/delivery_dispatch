package com.girdharshukla.deliverymatch.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.girdharshukla.deliverymatch.controllers.AgentController.AddAgentRequestDto;
import com.girdharshukla.deliverymatch.models.Agent;
import com.girdharshukla.deliverymatch.services.AgentService;
import com.girdharshukla.deliverymatch.services.JwtService;

@WebMvcTest(controllers = AgentController.class)
@AutoConfigureMockMvc(addFilters = false) // security filters off for now
class AgentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AgentService agentService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void addAgentReturns200WithSavedAgent() throws Exception {
        AddAgentRequestDto dto = new AddAgentRequestDto(12.34, 56.78, 1, 0);

        Agent agent = new Agent();
        agent.setId(UUID.randomUUID());
        agent.setLatitude(12.34);
        agent.setLongitude(56.78);
        agent.setCapacity(1);
        agent.setCurrentLoad(0);
        agent.setStatus(Agent.Status.IDLE);
        agent.setCreatedAt(LocalDateTime.now());

        when(agentService.saveAgent(dto)).thenReturn(agent);

        mockMvc.perform(post("/agents/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latitude").value(12.34))
                .andExpect(jsonPath("$.status").value("IDLE"));
    }

    @Test
    void addAgentReturns409WhenUserAlreadyAgent() throws Exception {
        AddAgentRequestDto dto = new AddAgentRequestDto(12.34, 56.78, 1, 0);

        when(agentService.saveAgent(dto))
                .thenThrow(new IllegalStateException("User already exists as an agent"));

        mockMvc.perform(post("/agents/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(content().string("User already exists as an agent"));
    }
}