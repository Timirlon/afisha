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
    Page<Event> findAllByInitiator_IdInAndStateInAndCategory_IdInAndDateBetween(Collection<Integer> initiatorsIds,
                                                                                Collection<PublicationState> states,
                                                                                Collection<Integer> categoryIds,
                                                                                LocalDateTime rangeStart,
                                                                                LocalDateTime rangeEnd,
                                                                                Pageable pageable);

    Page<Event> findAllByStateInAndCategory_IdInAndDateBetween(Collection<PublicationState> states,
                                                               Collection<Integer> categoryIds,
                                                               LocalDateTime rangeStart,
                                                               LocalDateTime rangeEnd,
                                                               Pageable pageable);

    Page<Event> findAllByCategory_IdInAndDateBetween(Collection<Integer> categoryIds,
                                                     LocalDateTime rangeStart,
                                                     LocalDateTime rangeEnd,
                                                     Pageable pageable);

    Page<Event> findAllByDateBetween(LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd,
                                     Pageable pageable);


    Page<Event> findAllByInitiator_IdInAndCategory_IdInAndDateBetween(Collection<Integer> initiatorsIds,
                                                                      Collection<Integer> categoryIds,
                                                                      LocalDateTime rangeStart,
                                                                      LocalDateTime rangeEnd,
                                                                      Pageable pageable);

    Page<Event> findAllByInitiator_IdInAndDateBetween(Collection<Integer> initiatorsIds,
                                                      LocalDateTime rangeStart,
                                                      LocalDateTime rangeEnd,
                                                      Pageable pageable);

    Page<Event> findAllByInitiator_IdIn(Collection<Integer> initiatorsIds,
                                        Pageable pageable);

    Page<Event> findAllByInitiator_IdInAndStateInAndDateBetween(Collection<Integer> initiatorsIds,
                                                                Collection<PublicationState> states,
                                                                LocalDateTime rangeStart,
                                                                LocalDateTime rangeEnd,
                                                                Pageable pageable);

    Page<Event> findAllByInitiator_IdInAndStateIn(Collection<Integer> initiatorsIds,
                                                  Collection<PublicationState> states,
                                                  Pageable pageable);

    Page<Event> findAllByInitiator_IdInAndStateInAndCategory_IdIn(Collection<Integer> initiatorsIds,
                                                                  Collection<PublicationState> states,
                                                                  Collection<Integer> categoryIds,
                                                                  Pageable pageable);

    // GET /events
    // onlyAvailable=false && sort=EVENT_DATE
    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndPaidIsAndDateBetweenAndStateIsOrderByDate(String annotationText,
                                                                                                                                              String titleText,
                                                                                                                                              Collection<Integer> categoriesIds,
                                                                                                                                              Boolean isPaid,
                                                                                                                                              LocalDateTime rangeStart,
                                                                                                                                              LocalDateTime rangeEnd,
                                                                                                                                              PublicationState state,
                                                                                                                                              Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndPaidIsAndDateBetweenAndStateIsOrderByDate(String annotationText,
                                                                                                                               String titleText,
                                                                                                                               Boolean isPaid,
                                                                                                                               LocalDateTime rangeStart,
                                                                                                                               LocalDateTime rangeEnd,
                                                                                                                               PublicationState state,
                                                                                                                               Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndDateBetweenAndStateIsOrderByDate(String annotationText,
                                                                                                                      String titleText,
                                                                                                                      LocalDateTime rangeStart,
                                                                                                                      LocalDateTime rangeEnd,
                                                                                                                      PublicationState state,
                                                                                                                      Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndDateAfterAndStateIsOrderByDate(String annotationText,
                                                                                                                    String titleText,
                                                                                                                    LocalDateTime rangeStart,
                                                                                                                    PublicationState state,
                                                                                                                    Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndDateBetweenAndStateIsOrderByDate(String annotationText,
                                                                                                                                     String titleText,
                                                                                                                                     Collection<Integer> categoriesIds,
                                                                                                                                     LocalDateTime rangeStart,
                                                                                                                                     LocalDateTime rangeEnd,
                                                                                                                                     PublicationState state,
                                                                                                                                     Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndDateAfterAndStateIsOrderByDate(String annotationText,
                                                                                                                                   String titleText,
                                                                                                                                   Collection<Integer> categoriesIds,
                                                                                                                                   LocalDateTime rangeStart,
                                                                                                                                   PublicationState state,
                                                                                                                                   Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndPaidIsAndDateAfterAndStateIsOrderByDate(String annotationText,
                                                                                                                                            String titleText,
                                                                                                                                            Collection<Integer> categoriesIds,
                                                                                                                                            Boolean isPaid,
                                                                                                                                            LocalDateTime rangeStart,
                                                                                                                                            PublicationState state,
                                                                                                                                            Pageable pageable);

    // onlyAvailable=false && sort=VIEWS
    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndPaidIsAndDateBetweenAndStateIsOrderByViews(String annotationText,
                                                                                                                                               String titleText,
                                                                                                                                               Collection<Integer> categoriesIds,
                                                                                                                                               Boolean isPaid,
                                                                                                                                               LocalDateTime rangeStart,
                                                                                                                                               LocalDateTime rangeEnd,
                                                                                                                                               PublicationState state,
                                                                                                                                               Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndPaidIsAndDateBetweenAndStateIsOrderByViews(String annotationText,
                                                                                                                                String titleText,
                                                                                                                                Boolean isPaid,
                                                                                                                                LocalDateTime rangeStart,
                                                                                                                                LocalDateTime rangeEnd,
                                                                                                                                PublicationState state,
                                                                                                                                Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndDateBetweenAndStateIsOrderByViews(String annotationText,
                                                                                                                       String titleText,
                                                                                                                       LocalDateTime rangeStart,
                                                                                                                       LocalDateTime rangeEnd,
                                                                                                                       PublicationState state,
                                                                                                                       Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndDateAfterAndStateIsOrderByViews(String annotationText,
                                                                                                                     String titleText,
                                                                                                                     LocalDateTime rangeStart,
                                                                                                                     PublicationState state,
                                                                                                                     Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndDateBetweenAndStateIsOrderByViews(String annotationText,
                                                                                                                                      String titleText,
                                                                                                                                      Collection<Integer> categoriesIds,
                                                                                                                                      LocalDateTime rangeStart,
                                                                                                                                      LocalDateTime rangeEnd,
                                                                                                                                      PublicationState state,
                                                                                                                                      Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndDateAfterAndStateIsOrderByViews(String annotationText,
                                                                                                                                    String titleText,
                                                                                                                                    Collection<Integer> categoriesIds,
                                                                                                                                    LocalDateTime rangeStart,
                                                                                                                                    PublicationState state,
                                                                                                                                    Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndPaidIsAndDateAfterAndStateIsOrderByViews(String annotationText,
                                                                                                                                             String titleText,
                                                                                                                                             Collection<Integer> categoriesIds,
                                                                                                                                             Boolean isPaid,
                                                                                                                                             LocalDateTime rangeStart,
                                                                                                                                             PublicationState state,
                                                                                                                                             Pageable pageable);

    // onlyAvailable=true && sort=EVENT_DATE
    @Query("""
    SELECT e FROM Event e
    WHERE (LOWER(e.annotation) LIKE %:text% OR LOWER(e.title) LIKE %:text%)
        AND e.category.id IN :categoriesIds
        AND (:isPaid = null OR e.paid = :isPaid)
        AND e.date >= :rangeStart
        AND (:rangeEnd = null OR e.date <= :rangeEnd)
        AND e.confirmedRequests < e.participantLimit
        AND e.state = :state
    ORDER BY e.date
    """)
    Page<Event> findOnlyAvailableByMultipleParamsOrderByEventDate(String text,
                                                                  Collection<Integer> categoriesIds,
                                                                  Boolean isPaid,
                                                                  LocalDateTime rangeStart,
                                                                  LocalDateTime rangeEnd,
                                                                  PublicationState state,
                                                                  Pageable pageable);


    // onlyAvailable=true && sort=VIEWS
    @Query("""
    SELECT e FROM Event e
    WHERE (LOWER(e.annotation) LIKE %:text% OR LOWER(e.title) LIKE %:text%)
        AND e.category.id IN :categoriesIds
        AND (:isPaid = null OR e.paid = :isPaid)
        AND e.date >= :rangeStart
        AND (:rangeEnd = null OR e.date <= :rangeEnd)
        AND e.confirmedRequests < e.participantLimit
        AND e.state = :state
    """)
    Page<Event> findOnlyAvailableByMultipleParamsOrderByViews(String text,
                                                              Collection<Integer> categoriesIds,
                                                              Boolean isPaid,
                                                              LocalDateTime rangeStart,
                                                              LocalDateTime rangeEnd,
                                                              PublicationState state,
                                                              Pageable pageable);


    // onlyAvailable=false && sort=null
    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndPaidIsAndDateBetweenAndStateIs(String annotationText,
                                                                                                                                   String titleText,
                                                                                                                                   Collection<Integer> categoriesIds,
                                                                                                                                   Boolean isPaid,
                                                                                                                                   LocalDateTime rangeStart,
                                                                                                                                   LocalDateTime rangeEnd,
                                                                                                                                   PublicationState state,
                                                                                                                                   Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndPaidIsAndDateBetweenAndStateIs(String annotationText,
                                                                                                                    String titleText,
                                                                                                                    Boolean isPaid,
                                                                                                                    LocalDateTime rangeStart,
                                                                                                                    LocalDateTime rangeEnd,
                                                                                                                    PublicationState state,
                                                                                                                    Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndDateBetweenAndStateIs(String annotationText,
                                                                                                           String titleText,
                                                                                                           LocalDateTime rangeStart,
                                                                                                           LocalDateTime rangeEnd,
                                                                                                           PublicationState state,
                                                                                                           Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndDateAfterAndStateIs(String annotationText,
                                                                                                         String titleText,
                                                                                                         LocalDateTime rangeStart,
                                                                                                         PublicationState state,
                                                                                                         Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndDateBetweenAndStateIs(String annotationText,
                                                                                                                          String titleText,
                                                                                                                          Collection<Integer> categoriesIds,
                                                                                                                          LocalDateTime rangeStart,
                                                                                                                          LocalDateTime rangeEnd,
                                                                                                                          PublicationState state,
                                                                                                                          Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndDateAfterAndStateIs(String annotationText,
                                                                                                                        String titleText,
                                                                                                                        Collection<Integer> categoriesIds,
                                                                                                                        LocalDateTime rangeStart,
                                                                                                                        PublicationState state,
                                                                                                                        Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndPaidIsAndDateAfterAndStateIs(String annotationText,
                                                                                                                                 String titleText,
                                                                                                                                 Collection<Integer> categoriesIds,
                                                                                                                                 Boolean isPaid,
                                                                                                                                 LocalDateTime rangeStart,
                                                                                                                                 PublicationState state,
                                                                                                                                 Pageable pageable);


    // onlyAvailable=true && sort=null
    @Query("""
    SELECT e FROM Event e
    WHERE (LOWER(e.annotation) LIKE %:text% OR LOWER(e.title) LIKE %:text%)
        AND e.category.id IN :categoriesIds
        AND (:isPaid = null OR e.paid = :isPaid)
        AND e.date >= :rangeStart
        AND (:rangeEnd = null OR e.date <= :rangeEnd)
        AND e.confirmedRequests < e.participantLimit
        AND e.state = :state
    """)
    Page<Event> findOnlyAvailableByMultipleParamsNotOrdered(String text,
                                                            Collection<Integer> categoriesIds,
                                                            Boolean isPaid,
                                                            LocalDateTime rangeStart,
                                                            LocalDateTime rangeEnd,
                                                            PublicationState state,
                                                            Pageable pageable);

    Optional<Event> findByIdAndState(int id, PublicationState state);


    List<Event> findAllByIdIn(Collection<Integer> ids);
}
