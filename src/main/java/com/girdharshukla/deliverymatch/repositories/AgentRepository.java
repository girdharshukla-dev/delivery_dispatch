package com.girdharshukla.deliverymatch.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.girdharshukla.deliverymatch.models.Agent;

@Repository
public interface AgentRepository extends JpaRepository<Agent, UUID>{
    
}
