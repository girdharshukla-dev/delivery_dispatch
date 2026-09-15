package com.girdharshukla.deliverymatch.controllers;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.girdharshukla.deliverymatch.models.Agent.Status;
import com.girdharshukla.deliverymatch.services.AgentService;

@RestController
@RequestMapping("/agents")
public class AgentController {
    private final AgentService agentService;

    public AgentController(AgentService agentService){
        this .agentService = agentService;
    }

    public record AddAgentRequestDto(
        double latitude,
        double longitude,
        int capacity,
        int currentLoad
    ){}

    @PostMapping("/add")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<?> addAgent(@RequestBody AddAgentRequestDto agentDto) throws IOException{
        try{
            return ResponseEntity.ok(agentService.saveAgent(agentDto));
        }catch(IllegalStateException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/status")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<?> updateStatus(@RequestBody String status){
        if (agentService.updateStatus(Status.valueOf(status)) > 0){
            return ResponseEntity.ok(status);
        }else{
            return ResponseEntity.status(400).body("Failed to update status");
        }
    }

    public record UpdateLocReqDto(double latitude, double longitude) {}
    @PostMapping("/location")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<?> updateLocation(@RequestBody UpdateLocReqDto updateLocReqDto){
        if (agentService.updateLocation(updateLocReqDto) > 0){
            return ResponseEntity.ok(updateLocReqDto);
        }else{
            return ResponseEntity.status(400).body("Failed to update status");
        }
    }

}


