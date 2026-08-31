package com.girdharshukla.deliverymatch.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.girdharshukla.deliverymatch.models.Agent;

@Repository
public interface AgentRepository extends JpaRepository<Agent, UUID>{
    List<Agent> findByStatus(Agent.Status status); 

    @Query("SELECT a FROM Agent a WHERE a.status = :status AND a.currentLoad < a.capacity")
    List<Agent> findAvailableAgents(@Param("status") Agent.Status status);
}
