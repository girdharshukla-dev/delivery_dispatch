package com.girdharshukla.deliverymatch.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.girdharshukla.deliverymatch.controllers.AgentController.AddAgentRequestDto;
import com.girdharshukla.deliverymatch.models.Agent;
import com.girdharshukla.deliverymatch.repositories.AgentRepository;
import com.uber.h3core.H3Core;

@Service
public class AgentService {
    
    private final AgentRepository agentRepository;
    private final H3Core h3Core;

    public AgentService(AgentRepository agentRepository, H3Core h3Core){
        this.agentRepository = agentRepository;
        this.h3Core = h3Core;
    }

    public Agent saveAgent(AddAgentRequestDto agentDto) throws IOException{

        Agent agent = new Agent();
        
        agent.setId(UUID.randomUUID());
        agent.setLatitude(agentDto.latitude());
        agent.setLongitude(agentDto.longitude());
        agent.setCapacity(agentDto.capacity());
        agent.setCurrentLoad(agentDto.currentLoad());
        agent.setH3Cell(h3Core.latLngToCell(agentDto.latitude(), agentDto.longitude(), 3));
        agent.setCreatedAt(LocalDateTime.now());

        return agentRepository.save(agent);
    }

}
