package com.girdharshukla.deliverymatch.services;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.girdharshukla.deliverymatch.controllers.AgentController.AgentRequestDto;
import com.girdharshukla.deliverymatch.models.Agent;
import com.girdharshukla.deliverymatch.repositories.AgentRepository;
import com.uber.h3core.H3Core;

@Service
public class AgentService {
    
    private final AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository){
        this.agentRepository = agentRepository;
    }

    public Agent saveAgent(AgentRequestDto agentDto) throws IOException{

        H3Core h3 = H3Core.newInstance();

        Agent agent = new Agent();
        
        agent.setId(UUID.randomUUID());
        agent.setLatitude(agentDto.latitude());
        agent.setLongitude(agentDto.longitude());
        agent.setCapacity(agentDto.capacity());
        agent.setCurrentLoad(agentDto.currentLoad());
        agent.setH3Cell(h3.latLngToCell(agentDto.latitude(), agentDto.longitude(), 3));

        return agentRepository.save(agent);
    }

}
