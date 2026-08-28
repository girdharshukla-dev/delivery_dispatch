package com.girdharshukla.deliverymatch.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "agents")
public class Agent {

    public enum Status{
        IDLE,
        BUSY,
        INACTIVE
    };

    @Id
    @Column
    private UUID id;

    @Column
    private double latitude;
    
    @Column
    private double longitude;

    @Column(name = "h3_cell")
    private Long h3Cell;

    @Column
    private int capacity;

    @Column(name = "current_load")
    private int currentLoad;

    @Enumerated(EnumType.STRING)
    @Column
    private Status status = Status.IDLE;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Long getH3Cell() {
        return h3Cell;
    }

    public void setH3Cell(Long h3Cell) {
        this.h3Cell = h3Cell;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }

    public void setCurrentLoad(int currentLoad) {
        this.currentLoad = currentLoad;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
