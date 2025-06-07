package com.practice.afisha.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String title;

    String annotation;

    String description;

    @Column(name = "participant_limit")
    Integer participantLimit;

    LocalDateTime date;

    @Column(name = "location_lon")
    Double locationLongitude;

    @Column(name = "location_lat")
    Double locationLatitude;

    Boolean paid;

    @Column(name = "request_moderation")
    Boolean requestModeration;

    @Enumerated(value = EnumType.STRING)
    PublicationState state;

    @ManyToOne
    Category category;

    @ManyToOne
    User initiator;

    LocalDateTime created;

    LocalDateTime published;

    Integer views;

    @Column(name = "confirmed_requests")
    Integer confirmedRequests;

    @OneToMany(mappedBy = "event")
    final List<Request> requests = new ArrayList<>();

    @ManyToMany
    final List<Compilation> compilations = new ArrayList<>();
}
