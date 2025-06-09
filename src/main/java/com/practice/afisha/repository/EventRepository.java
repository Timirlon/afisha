package com.practice.afisha.repository;

import com.practice.afisha.model.Event;
import com.practice.afisha.model.PublicationState;
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

    Page<Event> findAllByInitiator_IdInAndStateInAndCategory_IdInAndDateBetween(Collection<Integer> initiatorsIds,
                                                                                Collection<PublicationState> states,
                                                                                Collection<Integer> categoryIds,
                                                                                LocalDateTime rangeStart,
                                                                                LocalDateTime rangeEnd,
                                                                                Pageable pageable);


    // GET /events
    // onlyAvailable=false && sort=EVENT_DATE
    @Query("""
    SELECT e FROM Event e
    WHERE (LOWER(e.annotation) LIKE %:text% OR LOWER(e.title) LIKE %:text%)
        AND e.category.id IN :categoriesIds
        AND e.paid = :isPaid
        AND e.date >= :rangeStart
        AND (:rangeEnd = null OR e.date <= :rangeEnd)
        AND e.state = :state
    ORDER BY e.date
    """)
    Page<Event> findAllByMultipleParametersOrderByDate(String text,
                                                       Collection<Integer> categoriesIds,
                                                       boolean isPaid,
                                                       LocalDateTime rangeStart,
                                                       LocalDateTime rangeEnd,
                                                       PublicationState state,
                                                       Pageable pageable);

    // onlyAvailable=false && sort=VIEWS
    @Query("""
    SELECT e FROM Event e
    WHERE (LOWER(e.annotation) LIKE %:text% OR LOWER(e.title) LIKE %:text%)
        AND e.category.id IN :categoriesIds
        AND e.paid = :isPaid
        AND e.date >= :rangeStart
        AND (:rangeEnd = null OR e.date <= :rangeEnd)
        AND e.state = :state
    ORDER BY e.views
    """)
    Page<Event> findAllByMultipleParametersOrderByViews(String text,
                                                        Collection<Integer> categoriesIds,
                                                        boolean isPaid,
                                                        LocalDateTime rangeStart,
                                                        LocalDateTime rangeEnd,
                                                        PublicationState state,
                                                        Pageable pageable);

    // onlyAvailable=true && sort=EVENT_DATE
    @Query("""
    SELECT e FROM Event e
    WHERE (LOWER(e.annotation) LIKE %:text% OR LOWER(e.title) LIKE %:text%)
        AND e.category.id IN :categoriesIds
        AND e.paid = :isPaid
        AND e.date >= :rangeStart
        AND (:rangeEnd = null OR e.date <= :rangeEnd)
        AND e.confirmedRequests < e.participantLimit
        AND e.state = :state
    ORDER BY e.date
    """)
    Page<Event> findOnlyAvailableByMultipleParamsOrderByEventDate(String text,
                                                                  Collection<Integer> categoriesIds,
                                                                  boolean isPaid,
                                                                  LocalDateTime rangeStart,
                                                                  LocalDateTime rangeEnd,
                                                                  PublicationState state,
                                                                  Pageable pageable);


    // onlyAvailable=true && sort=VIEWS
    @Query("""
    SELECT e FROM Event e
    WHERE (LOWER(e.annotation) LIKE %:text% OR LOWER(e.title) LIKE %:text%)
        AND e.category.id IN :categoriesIds
        AND e.paid = :isPaid
        AND e.date >= :rangeStart
        AND (:rangeEnd = null OR e.date <= :rangeEnd)
        AND e.confirmedRequests < e.participantLimit
        AND e.state = :state
    ORDER BY e.views
    """)
    Page<Event> findOnlyAvailableByMultipleParametersOrderByViews(String text,
                                                                  Collection<Integer> categoriesIds,
                                                                  boolean isPaid,
                                                                  LocalDateTime rangeStart,
                                                                  LocalDateTime rangeEnd,
                                                                  PublicationState state,
                                                                  Pageable pageable);

    Optional<Event> findByIdAndState(int id, PublicationState state);


    List<Event> findAllByIdIn(Collection<Integer> ids);
}
