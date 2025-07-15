package com.practice.mainsvc.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;

    String email;

    @OneToMany(mappedBy = "initiator")
    final List<Event> event = new ArrayList<>();

    @OneToMany(mappedBy = "requester")
    final List<ParticipationRequest> requests = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    final List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "reporter")
    final List<CommentReport> reports = new ArrayList<>();
}
