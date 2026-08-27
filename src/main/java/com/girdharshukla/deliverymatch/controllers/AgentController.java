package com.girdharshukla.deliverymatch.controllers;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.girdharshukla.deliverymatch.models.Agent;
import com.girdharshukla.deliverymatch.services.AgentService;

@RestController
@RequestMapping("/agents")
public class AgentController {
    private final AgentService agentService;

    public AgentController(AgentService agentService){
        this.agentService = agentService;
    }

    public record AgentRequestDto(
        double latitude,
        double longitude,
        int capacity,
        int currentLoad
    ){}

    @PostMapping("/add")
    public Agent agent(@RequestBody AgentRequestDto agentDto) throws IOException{
        return agentService.saveAgent(agentDto);
    }

}
