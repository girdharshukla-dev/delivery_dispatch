package com.girdharshukla.deliverymatch.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.girdharshukla.deliverymatch.controllers.AgentController.AddAgentRequestDto;
import com.girdharshukla.deliverymatch.models.Agent;
import com.girdharshukla.deliverymatch.models.User;
import com.girdharshukla.deliverymatch.repositories.AgentRepository;
import com.girdharshukla.deliverymatch.repositories.UserRepository;
import com.uber.h3core.H3Core;

@Service
public class AgentService {
    
    private final AgentRepository agentRepository;
    private final H3Core h3Core;
    private final UserRepository userRepository;

    @Value("${h3.resolution}") int resolution;

    public AgentService(AgentRepository agentRepository, H3Core h3Core, UserRepository userRepository){
        this.agentRepository = agentRepository;
        this.h3Core = h3Core;
        this.userRepository = userRepository;
    }

    public Agent saveAgent(AddAgentRequestDto agentDto) throws IOException{

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email);

        Agent agent = new Agent();
        
        agent.setId(UUID.randomUUID());
        agent.setLatitude(agentDto.latitude());
        agent.setLongitude(agentDto.longitude());
        agent.setCapacity(agentDto.capacity());
        agent.setCurrentLoad(agentDto.currentLoad());
        agent.setUser(user);
        if(agent.getCurrentLoad() >= agent.getCapacity()){
            agent.setStatus(Agent.Status.BUSY);
        } 
        agent.setH3Cell(h3Core.latLngToCell(agentDto.latitude(), agentDto.longitude(), resolution));
        agent.setCreatedAt(LocalDateTime.now());

        return agentRepository.save(agent);
    }

}

