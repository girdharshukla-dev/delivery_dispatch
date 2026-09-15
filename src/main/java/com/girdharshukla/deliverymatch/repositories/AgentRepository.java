package com.girdharshukla.deliverymatch.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.girdharshukla.deliverymatch.models.Agent;
import com.girdharshukla.deliverymatch.models.User;

@Repository
public interface AgentRepository extends JpaRepository<Agent, UUID> {
    List<Agent> findByStatus(Agent.Status status);

    @Query("SELECT a FROM Agent a WHERE a.status = :status AND a.currentLoad < a.capacity")
    List<Agent> findAvailableAgents(@Param("status") Agent.Status status);

    Optional<Agent> findByUser(User user);

    Optional<Agent> findByUserId(UUID id);

    @Modifying
    @Query("UPDATE Agent a SET a.status = :status WHERE a.user.id = :userId")
    int updateStatusById(
            @Param("userId") UUID userId,
            @Param("status") Agent.Status status);

    @Modifying
    @Query("UPDATE Agent a SET a.latitude = :latitude, a.longitude = :longitude WHERE a.user.id = :userId")
    int updateLocationById(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("userId") UUID userId);

}
