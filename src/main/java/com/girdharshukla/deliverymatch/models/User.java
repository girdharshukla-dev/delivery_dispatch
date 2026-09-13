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
@Table(name = "users")
public class User {

  public enum Role {
    USER,
    AGENT,
    ADMIN
  };

  @Id
  @Column
  private UUID id;

  @Column
  private String email;

  @Column
  private String password;

  @Enumerated(EnumType.STRING)
  @Column
  private Role role;

  @Column
  private LocalDateTime createdAt;

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return this.id;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return this.email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return this.password;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public Role getRole() {
    return this.role;
  }

}
