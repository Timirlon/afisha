package com.practice.statssvc.repository;

import com.practice.statssvc.dto.ViewStats;
import com.practice.statssvc.model.Hit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;

public interface HitRepository extends JpaRepository<Hit, Integer> {

    // uris empty, unique=false
    @Query("""
    SELECT new com.practice.statssvc.dto.ViewStats(h.app, h.uri, COUNT(h.ip))
    FROM Hit h
    WHERE h.created BETWEEN :start AND :end
    GROUP BY h.app, h.uri
    ORDER BY COUNT(h.ip) DESC
    """)
    Collection<ViewStats> findAllByCreatedInRange(LocalDateTime start, LocalDateTime end);


    // uris not empty, unique=false
    @Query("""
    SELECT new com.practice.statssvc.dto.ViewStats(h.app, h.uri, COUNT(h.ip))
    FROM Hit h
    WHERE h.created BETWEEN :start AND :end
    AND h.uri IN :uris
    GROUP BY h.app, h.uri
    ORDER BY COUNT(h.ip) DESC
    """)
    Collection<ViewStats> findAllByCreatedInRangeAndUriIn(LocalDateTime start,
                                                          LocalDateTime end,
                                                          String[] uris);


    // uris empty, unique=true
    @Query("""
    SELECT new com.practice.statssvc.dto.ViewStats(h.app, h.uri, COUNT(DISTINCT h.ip))
    FROM Hit h
    WHERE h.created BETWEEN :start AND :end
    GROUP BY h.uri, h.app
    ORDER BY COUNT(h.ip) DESC
    """)
    Collection<ViewStats> findAllByCreatedInRangeAndUniqueTrue(LocalDateTime start,
                                                               LocalDateTime end);


    // uris not empty, unique=true
    @Query("""
    SELECT new com.practice.statssvc.dto.ViewStats(h.app, h.uri, COUNT(DISTINCT h.ip))
    FROM Hit h
    WHERE h.created BETWEEN :start AND :end
    AND h.uri IN :uris
    GROUP BY h.uri, h.app
    ORDER BY COUNT(h.ip) DESC
    """)
    Collection<ViewStats> findAllByCreatedInRangeAndUriInAndUniqueTrue(LocalDateTime start,
                                                                       LocalDateTime end,
                                                                       String[] uris);
}
