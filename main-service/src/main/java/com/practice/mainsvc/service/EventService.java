package com.practice.mainsvc.service;

import com.practice.mainsvc.exception.RequestInputException;
import com.practice.mainsvc.util.AdminStateAction;
import com.practice.mainsvc.util.EventSort;
import com.practice.mainsvc.util.UserStateAction;
import com.practice.mainsvc.exception.InvalidConditionException;
import com.practice.mainsvc.exception.NotFoundException;
import com.practice.mainsvc.model.Category;
import com.practice.mainsvc.model.Event;
import com.practice.mainsvc.model.PublicationState;
import com.practice.mainsvc.model.User;
import com.practice.mainsvc.repository.CategoryRepository;
import com.practice.mainsvc.repository.EventRepository;
import com.practice.mainsvc.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static com.practice.mainsvc.util.DateTimeFormatConstants.parse;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor

@Service
public class EventService {
    EventRepository eventRepository;
    UserRepository userRepository;
    CategoryRepository categoryRepository;

    public Page<Event> findAllByInitiatorId(int initiatorId, int from, int size) {
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);


        return eventRepository.findAllByInitiator_Id(initiatorId, pageable);
    }

    public Event create(Event event, int initiatorId, int categoryId) {
        User user = findUserById(initiatorId);

        Category category = findCategoryById(categoryId);

        checkIfDateCorrectOrElseThrow(event.getDate());


        event.setInitiator(user);
        event.setCategory(category);
        event.setCreated(LocalDateTime.now());
        event.setState(PublicationState.PENDING);


        eventRepository.save(event);


        return event;
    }

    public Event findByIdPublicRequest(int eventId, int userId) {
        findUserById(userId);

        return eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event with id=%d was not found", eventId)));
    }

    public Event updateByIdUserRequest(int eventId, Event updateEvent,  int userId,
                                       Integer newCategoryId, UserStateAction stateAction) {
        User requester = findUserById(userId);

        Event foundEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event with id=%d was not found", eventId)));


        // Автор запроса и инициатор события - один пользователь
        if (userId != foundEvent.getInitiator().getId()) {
            throw new NotFoundException(
                    String.format("Event with id=%d was not found", eventId));
        }

        if (foundEvent.getState() == PublicationState.PUBLISHED) {
            throw new InvalidConditionException("Only pending or canceled events can be changed");
        }


        // Обновление полей
        if (stateAction == UserStateAction.SEND_TO_REVIEW) {

            foundEvent.setState(PublicationState.PENDING);
        } else if (stateAction == UserStateAction.CANCEL_REVIEW) {

            foundEvent.setState(PublicationState.CANCELED);
        }

        if (newCategoryId != null) {
            Category category = findCategoryById(newCategoryId);
            foundEvent.setCategory(category);
        }

        if (updateEvent.getDate() != null) {
            checkIfDateCorrectOrElseThrow(
                    updateEvent.getDate());

            foundEvent.setDate(
                    updateEvent.getDate());
        }

        if (updateEvent.getAnnotation() != null && !updateEvent.getAnnotation().isBlank()) {
            foundEvent.setAnnotation(
                    updateEvent.getAnnotation());
        }

        if (updateEvent.getDescription() != null && !updateEvent.getDescription().isBlank()) {
            foundEvent.setDescription(
                    updateEvent.getDescription());
        }

        if (updateEvent.getTitle() != null && !updateEvent.getTitle().isBlank()) {
            foundEvent.setTitle(updateEvent.getTitle());
        }

        if (updateEvent.getLocationLatitude() != null) {
            foundEvent.setLocationLatitude(updateEvent.getLocationLatitude());
        }

        if (updateEvent.getLocationLongitude() != null) {
            foundEvent.setLocationLongitude(updateEvent.getLocationLatitude());
        }

        if (updateEvent.getPaid() != null) {
            foundEvent.setPaid(updateEvent.getPaid());
        }

        if (updateEvent.getParticipantLimit() != null) {
            foundEvent.setParticipantLimit(updateEvent.getParticipantLimit());
        }

        if (updateEvent.getRequestModeration() != null) {
            foundEvent.setRequestModeration(updateEvent.getRequestModeration());
        }


        eventRepository.save(foundEvent);

        return foundEvent;
    }

    public Page<Event> findAllByMultipleParametersAdminRequest(Collection<Integer> initiatorIds,
                                                               Collection<String> eventStates,
                                                               Collection<Integer> categoryIds,
                                                               String rangeStart,
                                                               String rangeEnd,
                                                               int from,
                                                               int size) {


        List<PublicationState> convertedStates = List.of();
        if (eventStates != null) {
            convertedStates = eventStates.stream()
                    .map(this::getEventPublicationState)
                    .distinct()
                    .toList();
        }

        if (initiatorIds == null) {
            initiatorIds = List.of();
        }

        if (categoryIds == null) {
            categoryIds = List.of();
        }

        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null) {
            start = parse(rangeStart);
        }

        if (rangeEnd != null) {
            end = parse(rangeEnd);
        }


        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);


        if (!initiatorIds.isEmpty()
                && !convertedStates.isEmpty()
                && !categoryIds.isEmpty()
                && start != null
                && end != null) {
            return eventRepository.findAllByInitiator_IdInAndStateInAndCategory_IdInAndDateBetween(
                    initiatorIds, convertedStates, categoryIds, start, end, pageable);
        }

        if (initiatorIds.isEmpty()
                && !convertedStates.isEmpty()
                && !categoryIds.isEmpty()
                && start != null
                && end != null) {
            return eventRepository.findAllByStateInAndCategory_IdInAndDateBetween(
                    convertedStates, categoryIds, start, end, pageable);
        }

        if (initiatorIds.isEmpty()
                && convertedStates.isEmpty()
                && !categoryIds.isEmpty()
                && start != null
                && end != null) {
            return eventRepository.findAllByCategory_IdInAndDateBetween(
                    categoryIds, start, end, pageable);
        }

        if (initiatorIds.isEmpty()
                && convertedStates.isEmpty()
                && categoryIds.isEmpty()
                && start != null
                && end != null) {
            return eventRepository.findAllByDateBetween(
                    start, end, pageable);
        }

        if (initiatorIds.isEmpty()
                && convertedStates.isEmpty()
                && categoryIds.isEmpty()
                && start == null
                && end == null) {
            return eventRepository.findAll(
                    pageable);
        }

        if (!initiatorIds.isEmpty()
                && convertedStates.isEmpty()
                && !categoryIds.isEmpty()
                && start != null
                && end != null) {
            return eventRepository.findAllByInitiator_IdInAndCategory_IdInAndDateBetween(
                    initiatorIds, categoryIds, start, end, pageable);
        }

        if (!initiatorIds.isEmpty()
                && convertedStates.isEmpty()
                && categoryIds.isEmpty()
                && start != null
                && end != null) {
            return eventRepository.findAllByInitiator_IdInAndDateBetween(
                    initiatorIds, start, end, pageable);
        }

        if (!initiatorIds.isEmpty()
                && convertedStates.isEmpty()
                && categoryIds.isEmpty()
                && start == null
                && end == null) {
            return eventRepository.findAllByInitiator_IdIn(
                    initiatorIds, pageable);
        }

        if (!initiatorIds.isEmpty()
                && !convertedStates.isEmpty()
                && categoryIds.isEmpty()
                && start != null
                && end != null) {
            return eventRepository.findAllByInitiator_IdInAndStateInAndDateBetween(
                    initiatorIds, convertedStates, start, end, pageable);
        }

        if (!initiatorIds.isEmpty()
                && !convertedStates.isEmpty()
                && categoryIds.isEmpty()
                && start == null
                && end == null) {
            return eventRepository.findAllByInitiator_IdInAndStateIn(
                    initiatorIds, convertedStates, pageable);
        }

        if (!initiatorIds.isEmpty()
                && !convertedStates.isEmpty()
                && !categoryIds.isEmpty()
                && start == null
                && end == null) {
            return eventRepository.findAllByInitiator_IdInAndStateInAndCategory_IdIn(
                    initiatorIds, convertedStates, categoryIds, pageable);
        }

        return Page.empty();
    }

    public Event updateByIdAdminRequest(int eventId, Event updateEvent,
                                        int newCategoryId, AdminStateAction stateAction) {
        Event foundEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event with id=%d was not found", eventId)));

        if (foundEvent.getState() == PublicationState.PUBLISHED && stateAction == AdminStateAction.REJECT_EVENT) {
            throw new InvalidConditionException(
                    String.format("Cannot reject the event because it's not in the right state: %s", foundEvent.getState()));
        }

        if (foundEvent.getState() != PublicationState.PENDING && stateAction == AdminStateAction.PUBLISH_EVENT) {
            throw new InvalidConditionException(
                    String.format("Cannot publish the event because it's not in the right state: %s", foundEvent.getState()));
        }


        if (newCategoryId != 0) {
            Category category = findCategoryById(newCategoryId);
            foundEvent.setCategory(category);
        }

        if (updateEvent.getDate() != null) {
            checkIfDateCorrectOrElseThrow(
                    updateEvent.getDate());

            foundEvent.setDate(
                    updateEvent.getDate());
        }


        LocalDateTime now = LocalDateTime.now();

        if (stateAction == AdminStateAction.PUBLISH_EVENT
        && foundEvent.getDate().isBefore(now.plusHours(1))) {
            throw new InvalidConditionException("Cannot publish the event planned in less than 1 hour.");
        }

        if (stateAction == AdminStateAction.PUBLISH_EVENT) {
            foundEvent.setState(PublicationState.PUBLISHED);
            foundEvent.setPublished(now);

        } else if (stateAction == AdminStateAction.REJECT_EVENT) {
            foundEvent.setState(PublicationState.CANCELED);
        }


        if (updateEvent.getAnnotation() != null && !updateEvent.getAnnotation().isBlank()) {
            foundEvent.setAnnotation(
                    updateEvent.getAnnotation());
        }

        if (updateEvent.getDescription() != null && !updateEvent.getDescription().isBlank()) {
            foundEvent.setDescription(
                    updateEvent.getDescription());
        }

        if (updateEvent.getTitle() != null && !updateEvent.getTitle().isBlank()) {
            foundEvent.setTitle(updateEvent.getTitle());
        }

        if (updateEvent.getLocationLatitude() != null) {
            foundEvent.setLocationLatitude(updateEvent.getLocationLatitude());
        }

        if (updateEvent.getLocationLongitude() != null) {
            foundEvent.setLocationLongitude(updateEvent.getLocationLatitude());
        }

        if (updateEvent.getPaid() != null) {
            foundEvent.setPaid(updateEvent.getPaid());
        }

        if (updateEvent.getParticipantLimit() != null) {
            foundEvent.setParticipantLimit(updateEvent.getParticipantLimit());
        }

        if (updateEvent.getRequestModeration() != null) {
            foundEvent.setRequestModeration(updateEvent.getRequestModeration());
        }

        eventRepository.save(foundEvent);


        return foundEvent;
    }

    public Page<Event> findAllByMultipleParametersPublicRequest(String searchText,
                                                                List<Integer> categoriesIds,
                                                                Boolean isPaid,
                                                                String rangeStartStr,
                                                                String rangeEndStr,
                                                                Boolean onlyAvailable,
                                                                EventSort sort,
                                                                int from,
                                                                int size) {
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);

        if (searchText == null || searchText.isBlank()) {
            searchText = "";
        }

        if (categoriesIds == null) {
            categoriesIds = List.of();
        }

        LocalDateTime start;
        LocalDateTime end;
        if (rangeStartStr != null && rangeEndStr != null
                && !rangeStartStr.isBlank() && !rangeEndStr.isBlank()) {

            start = parse(rangeStartStr);
            end = parse(rangeEndStr);
        } else {
            start = LocalDateTime.now();
            end = null;
        }

        if (end != null && end.isBefore(start)) {
            throw new RequestInputException("Range end must be after range start!");
        }

        PublicationState publishedState = PublicationState.PUBLISHED;



        if (!onlyAvailable && sort == EventSort.EVENT_DATE
                && !categoriesIds.isEmpty()
                && isPaid != null
                && end != null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndPaidIsAndDateBetweenAndStateIsOrderByDate(
                            searchText,
                            searchText,
                            categoriesIds,
                            isPaid,
                            start,
                            end,
                            publishedState,
                            pageable);
        }

        if (!onlyAvailable && sort == EventSort.EVENT_DATE
                && categoriesIds.isEmpty()
                && isPaid != null
                && end != null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndPaidIsAndDateBetweenAndStateIsOrderByDate(
                            searchText,
                            searchText,
                            isPaid,
                            start,
                            end,
                            publishedState,
                            pageable);
        }

        if (!onlyAvailable && sort == EventSort.EVENT_DATE
                && categoriesIds.isEmpty()
                && isPaid == null
                && end != null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndDateBetweenAndStateIsOrderByDate(
                            searchText,
                            searchText,
                            start,
                            end,
                            publishedState,
                            pageable);
        }

        if (!onlyAvailable && sort == EventSort.EVENT_DATE
                && categoriesIds.isEmpty()
                && isPaid == null
                && end == null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndDateAfterAndStateIsOrderByDate(
                            searchText,
                            searchText,
                            start,
                            publishedState,
                            pageable);
        }

        if (!onlyAvailable && sort == EventSort.EVENT_DATE
                && !categoriesIds.isEmpty()
                && isPaid == null
                && end != null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndDateBetweenAndStateIsOrderByDate(
                            searchText,
                            searchText,
                            categoriesIds,
                            start,
                            end,
                            publishedState,
                            pageable);
        }

        if (!onlyAvailable && sort == EventSort.EVENT_DATE
                && !categoriesIds.isEmpty()
                && isPaid == null
                && end == null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndDateAfterAndStateIsOrderByDate(
                            searchText,
                            searchText,
                            categoriesIds,
                            start,
                            publishedState,
                            pageable);
        }

        if (!onlyAvailable && sort == EventSort.EVENT_DATE
                && !categoriesIds.isEmpty()
                && isPaid != null
                && end == null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndPaidIsAndDateAfterAndStateIsOrderByDate(
                            searchText,
                            searchText,
                            categoriesIds,
                            isPaid,
                            start,
                            publishedState,
                            pageable);
        }

        if (!onlyAvailable && sort == EventSort.VIEWS
                && !categoriesIds.isEmpty()
                && isPaid != null
                && end != null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndPaidIsAndDateBetweenAndStateIsOrderByViews(
                            searchText,
                            searchText,
                            categoriesIds,
                            isPaid,
                            start,
                            end,
                            publishedState,
                            pageable);
        }

        if (!onlyAvailable && sort == EventSort.VIEWS
                && categoriesIds.isEmpty()
                && isPaid != null
                && end != null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndPaidIsAndDateBetweenAndStateIsOrderByViews(
                            searchText,
                            searchText,
                            isPaid,
                            start,
                            end,
                            publishedState,
                            pageable);
        }

        if (!onlyAvailable && sort == EventSort.VIEWS
                && categoriesIds.isEmpty()
                && isPaid == null
                && end != null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndDateBetweenAndStateIsOrderByViews(
                            searchText,
                            searchText,
                            start,
                            end,
                            publishedState,
                            pageable);
        }

        if (!onlyAvailable && sort == EventSort.VIEWS
                && categoriesIds.isEmpty()
                && isPaid == null
                && end == null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndDateAfterAndStateIsOrderByViews(
                            searchText,
                            searchText,
                            start,
                            publishedState,
                            pageable);
        }

        if (!onlyAvailable && sort == EventSort.VIEWS
                && !categoriesIds.isEmpty()
                && isPaid == null
                && end != null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndDateBetweenAndStateIsOrderByViews(
                            searchText,
                            searchText,
                            categoriesIds,
                            start,
                            end,
                            publishedState,
                            pageable);
        }

        if (!onlyAvailable && sort == EventSort.VIEWS
                && !categoriesIds.isEmpty()
                && isPaid == null
                && end == null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndDateAfterAndStateIsOrderByViews(
                            searchText,
                            searchText,
                            categoriesIds,
                            start,
                            publishedState,
                            pageable);
        }

        if (!onlyAvailable && sort == EventSort.VIEWS
                && !categoriesIds.isEmpty()
                && isPaid != null
                && end == null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndPaidIsAndDateAfterAndStateIsOrderByViews(
                            searchText,
                            searchText,
                            categoriesIds,
                            isPaid,
                            start,
                            publishedState,
                            pageable);
        }

        if (!onlyAvailable && sort == null
                && !categoriesIds.isEmpty()
                && isPaid != null
                && end != null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndPaidIsAndDateBetweenAndStateIs(
                            searchText,
                            searchText,
                            categoriesIds,
                            isPaid,
                            start,
                            end,
                            publishedState,
                            pageable);
        }

        if (!onlyAvailable && sort == null
                && categoriesIds.isEmpty()
                && isPaid != null
                && end != null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndPaidIsAndDateBetweenAndStateIs(
                            searchText,
                            searchText,
                            isPaid,
                            start,
                            end,
                            publishedState,
                            pageable);
        }

        if (!onlyAvailable && sort == null
                && categoriesIds.isEmpty()
                && isPaid == null
                && end != null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndDateBetweenAndStateIs(
                            searchText,
                            searchText,
                            start,
                            end,
                            publishedState,
                            pageable);
        }

        if (!onlyAvailable && sort == null
                && categoriesIds.isEmpty()
                && isPaid == null
                && end == null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndDateAfterAndStateIs(
                            searchText,
                            searchText,
                            start,
                            publishedState,
                            pageable);
        }

        if (!onlyAvailable && sort == null
                && !categoriesIds.isEmpty()
                && isPaid == null
                && end != null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndDateBetweenAndStateIs(
                            searchText,
                            searchText,
                            categoriesIds,
                            start,
                            end,
                            publishedState,
                            pageable);
        }

        if (!onlyAvailable && sort == null
                && !categoriesIds.isEmpty()
                && isPaid == null
                && end == null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndDateAfterAndStateIs(
                            searchText,
                            searchText,
                            categoriesIds,
                            start,
                            publishedState,
                            pageable);
        }

        if (!onlyAvailable && sort == null
                && !categoriesIds.isEmpty()
                && isPaid != null
                && end == null) {

            return eventRepository
                    .findAllByAnnotationContainingIgnoreCaseOrTitleContainingIgnoreCaseAndCategoryIdInAndPaidIsAndDateAfterAndStateIs(
                            searchText,
                            searchText,
                            categoriesIds,
                            isPaid,
                            start,
                            publishedState,
                            pageable);
        }

        if (onlyAvailable && sort == EventSort.EVENT_DATE) {

            return eventRepository
                    .findOnlyAvailableByMultipleParamsOrderByEventDate(
                            searchText,
                            categoriesIds,
                            isPaid,
                            start,
                            end,
                            publishedState,
                            pageable);
        }

        if (onlyAvailable && sort == EventSort.VIEWS) {

            return eventRepository
                    .findOnlyAvailableByMultipleParamsOrderByViews(
                            searchText,
                            categoriesIds,
                            isPaid,
                            start,
                            end,
                            publishedState,
                            pageable);
        }

        if (onlyAvailable && sort == null) {

            return eventRepository
                    .findOnlyAvailableByMultipleParamsNotOrdered(
                            searchText,
                            categoriesIds,
                            isPaid,
                            start,
                            end,
                            publishedState,
                            pageable);
        }


        return Page.empty();
    }

    public Event findByIdPublicRequest(int eventId) {
        return eventRepository.findByIdAndState(eventId, PublicationState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event with id=%d was not found", eventId)));
    }

    public Event updateConfirmedRequests(int eventId, int newConfirmedReq) {
        if (newConfirmedReq == 0) {
            return null;
        }

        Event foundEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event with id=%d was not found", eventId)));

        int updConfirmedRequests = foundEvent.getConfirmedRequests() + newConfirmedReq;
        foundEvent.setConfirmedRequests(updConfirmedRequests);

        eventRepository.save(foundEvent);

        return foundEvent;
    }

    private User findUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("User with id=%d was not found", userId)));
    }

    private Category findCategoryById(int categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Category with id=%d was not found", categoryId)));
    }

    private void checkIfDateCorrectOrElseThrow(LocalDateTime eventDate) {
        LocalDateTime now = LocalDateTime.now();
        int maxPossibleHours = 2;

        if (eventDate.isBefore(
                now.plusHours(maxPossibleHours))) {
            throw new RequestInputException(
                    String.format("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: %s",
                            eventDate));
        }
    }

    private PublicationState getEventPublicationState(String strState) {
        if (strState.equals(
                PublicationState.PENDING.name())) {
            return PublicationState.PENDING;
        }

        if (strState.equals(
                PublicationState.CANCELED.name())) {
            return PublicationState.CANCELED;
        }

        if (strState.equals(
                PublicationState.PUBLISHED.name())) {
            return PublicationState.PUBLISHED;
        }

        throw new RequestInputException("Invalid publication state provided.");
    }
}
