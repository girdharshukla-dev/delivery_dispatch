package com.girdharshukla.deliverymatch.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.girdharshukla.deliverymatch.models.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {
}
