package com.practice.mainsvc.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data

@Entity
@Table(name = "comment_reports")
public class CommentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String reason;

    @Column(name = "created_at")
    LocalDateTime created;

    @ManyToOne
    Comment comment;

    @ManyToOne
    User reporter;

    @Enumerated(value = EnumType.STRING)
    ReportStatus status;
}
