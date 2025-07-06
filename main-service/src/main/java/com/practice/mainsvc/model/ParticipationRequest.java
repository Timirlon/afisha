package com.practice.mainsvc.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "requests")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    LocalDateTime created;

    @Enumerated(value = EnumType.STRING)
    ConfirmationStatus status;

    @ManyToOne
    Event event;

    @ManyToOne
    User requester;
}
