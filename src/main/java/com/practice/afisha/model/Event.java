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
    int participantLimit;

    LocalDateTime date;

    @Column(name = "location_lon")
    int locationLongitude;

    @Column(name = "location_lat")
    int locationLatitude;

    boolean paid;

    @Column(name = "request_moderation")
    boolean requestModeration;

    PublicationState state;

    @ManyToOne
    Category category;

    @ManyToOne
    User initiator;

    LocalDateTime created;

    LocalDateTime published;

    int views;

    @Column(name = "confirmed_requests")
    int confirmedRequests;

    @OneToMany(mappedBy = "event")
    final List<Request> requests = new ArrayList<>();

    @ManyToMany
    final List<Compilation> compilations = new ArrayList<>();
}
