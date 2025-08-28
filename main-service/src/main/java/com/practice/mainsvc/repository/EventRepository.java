package com.practice.mainsvc.repository;

import com.practice.mainsvc.model.Event;
import com.practice.mainsvc.model.PublicationState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Page<Event> findAllByInitiator_Id(int initiatorId, Pageable pageable);

    Optional<Event> findByIdAndInitiator_Id(int eventId, int initiatorId);

    // GET /admin/events
    @Query("""
    SELECT e FROM Event e
    WHERE (:initiatorIds IS NULL OR e.initiator.id IN :initiatorIds)
        AND (:states IS NULL OR e.state IN :states)
        AND (:categoryIds IS NULL OR e.category.id IN :categoryIds)
        AND (CAST(:rangeStart AS TIMESTAMP) IS NULL OR e.date > :rangeStart)
        AND (CAST(:rangeEnd AS TIMESTAMP) IS NULL OR e.date < :rangeEnd)
    """)
    Page<Event> findAllByMultipleParamsAdminRequest(Collection<Integer> initiatorIds,
                                                    Collection<PublicationState> states,
                                                    Collection<Integer> categoryIds,
                                                    LocalDateTime rangeStart,
                                                    LocalDateTime rangeEnd,
                                                    Pageable pageable);

    // GET /events
    // sort=null
    @Query("""
    SELECT e FROM Event e
    WHERE (LOWER(e.annotation) LIKE LOWER(CONCAT('%', :searchText, '%'))
        OR LOWER(e.title) LIKE LOWER(CONCAT('%', :searchText, '%')))
        AND (:categoryIds IS NULL OR e.category.id IN (:categoryIds))
        AND (:isPaid IS NULL OR e.paid = :isPaid)
        AND (CAST(:rangeStart AS TIMESTAMP) IS NULL OR e.date > :rangeStart)
        AND (CAST(:rangeEnd AS TIMESTAMP) IS NULL OR e.date < :rangeEnd)
        AND (:onlyAvailable IS FALSE OR e.confirmedRequests < e.participantLimit)
        AND e.state = :state
    """)
    Page<Event> findAllByMultipleParamsPublicRequest(String searchText,
                                                     List<Integer> categoryIds,
                                                     Boolean isPaid,
                                                     LocalDateTime rangeStart,
                                                     LocalDateTime rangeEnd,
                                                     boolean onlyAvailable,
                                                     PublicationState state,
                                                     Pageable pageable);
    
    // sort=EVENT_DATE
    @Query("""
    SELECT e FROM Event e
    WHERE (LOWER(e.annotation) LIKE LOWER(CONCAT('%', :searchText, '%'))
        OR LOWER(e.title) LIKE LOWER(CONCAT('%', :searchText, '%')))
        AND (:categoryIds IS NULL OR e.category.id IN (:categoryIds))
        AND (:isPaid IS NULL OR e.paid = :isPaid)
        AND (CAST(:rangeStart AS TIMESTAMP) IS NULL OR e.date > :rangeStart)
        AND (CAST(:rangeEnd AS TIMESTAMP) IS NULL OR e.date < :rangeEnd)
        AND (:onlyAvailable IS FALSE OR e.confirmedRequests < e.participantLimit)
        AND e.state = :state
    ORDER BY e.date
    """)
    Page<Event> findAllByMultipleParamsPublicRequestOrderByDate(String searchText,
                                                                List<Integer> categoryIds,
                                                                Boolean isPaid,
                                                                LocalDateTime rangeStart,
                                                                LocalDateTime rangeEnd,
                                                                boolean onlyAvailable,
                                                                PublicationState state,
                                                                Pageable pageable);

    Optional<Event> findByIdAndState(int id, PublicationState state);


    List<Event> findAllByIdIn(Collection<Integer> ids);
}
