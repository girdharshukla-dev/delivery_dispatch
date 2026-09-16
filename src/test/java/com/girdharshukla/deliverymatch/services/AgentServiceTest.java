package com.girdharshukla.deliverymatch.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import com.girdharshukla.deliverymatch.controllers.AgentController.AddAgentRequestDto;
import com.girdharshukla.deliverymatch.models.Agent;
import com.girdharshukla.deliverymatch.models.User;
import com.girdharshukla.deliverymatch.models.UserPrincipal;
import com.girdharshukla.deliverymatch.repositories.AgentRepository;
import com.uber.h3core.H3Core;

public class AgentServiceTest {

    private AgentRepository agentRepository;
    private H3Core h3Core;
    private AgentService agentService;

    private MockedStatic<SecurityContextHolder> securityContextHolderMock;

    @BeforeEach
    void setup() {
        agentRepository = mock(AgentRepository.class);
        h3Core = mock(H3Core.class);

        agentService = new AgentService(agentRepository, h3Core);
        ReflectionTestUtils.setField(agentService, "resolution", 9);
        securityContextHolderMock = mockStatic(SecurityContextHolder.class);
    }

    @AfterEach
    void tearDown() {
        securityContextHolderMock.close();
    }

    @Test
    void savesNewAgentWhenUserIsNotAlreadyAgent() throws Exception {
        User user = new User();
        user.setId(UUID.randomUUID());
        UserPrincipal userPrincipal = new UserPrincipal(user);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);

        when(agentRepository.findByUser(user)).thenReturn(Optional.empty());
        when(h3Core.latLngToCell(12.34, 56.78, 9)).thenReturn(123456789L);
        when(agentRepository.save(any(Agent.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AddAgentRequestDto dto = new AddAgentRequestDto(12.34, 56.78, 1, 0);

        Agent savedAgent = agentService.saveAgent(dto);

        assertThat(savedAgent.getUser()).isEqualTo(user);
        assertThat(savedAgent.getLatitude()).isEqualTo(12.34);
        assertThat(savedAgent.getLongitude()).isEqualTo(56.78);
        assertThat(savedAgent.getH3Cell()).isEqualTo(123456789L);
        assertThat(savedAgent.getStatus()).isEqualTo(Agent.Status.IDLE);

        verify(agentRepository).save(any(Agent.class));
    }

}
