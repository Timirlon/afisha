package com.practice.mainsvc.model;

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
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String text;

    @ManyToOne
    Event event;

    @ManyToOne
    User author;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    Boolean hidden;

    @ManyToOne(fetch = FetchType.LAZY)
    Comment parent;

    @OneToMany(mappedBy = "parent")
    final List<Comment> subcomments = new ArrayList<>();
}
