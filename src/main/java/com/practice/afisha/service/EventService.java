package com.practice.afisha.service;

import com.practice.afisha.util.AdminStateAction;
import com.practice.afisha.util.EventSort;
import com.practice.afisha.util.UserStateAction;
import com.practice.afisha.exception.InvalidConditionException;
import com.practice.afisha.exception.NotFoundException;
import com.practice.afisha.model.Category;
import com.practice.afisha.model.Event;
import com.practice.afisha.model.PublicationState;
import com.practice.afisha.model.User;
import com.practice.afisha.repository.CategoryRepository;
import com.practice.afisha.repository.EventRepository;
import com.practice.afisha.repository.UserRepository;
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
                                                               Collection<PublicationState> eventStates,
                                                               Collection<Integer> categoryIds,
                                                               LocalDateTime rangeStart,
                                                               LocalDateTime rangeEnd,
                                                               int from,
                                                               int size) {
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);

        return eventRepository.findAllByInitiator_IdInAndStateInAndCategory_IdInAndDateBetween(
                initiatorIds, eventStates, categoryIds, rangeStart, rangeEnd, pageable);
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
                                                                boolean isPaid,
                                                                LocalDateTime rangeStart,
                                                                LocalDateTime rangeEnd,
                                                                boolean onlyAvailable,
                                                                EventSort sort,
                                                                int from,
                                                                int size) {
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);
        String lowerCaseText = searchText.toLowerCase();

        PublicationState publishedState = PublicationState.PUBLISHED;

        if (!onlyAvailable && sort == EventSort.EVENT_DATE) {
            return eventRepository.findAllByMultipleParametersOrderByDate(
                    lowerCaseText, categoriesIds, isPaid, rangeStart, rangeEnd, publishedState, pageable);
        }

        if (!onlyAvailable && sort == EventSort.VIEWS) {
            return eventRepository.findAllByMultipleParametersOrderByViews(
                    lowerCaseText, categoriesIds, isPaid, rangeStart, rangeEnd, publishedState, pageable);
        }

        if (onlyAvailable && sort == EventSort.EVENT_DATE) {
            return eventRepository.findOnlyAvailableByMultipleParamsOrderByEventDate(
                    lowerCaseText, categoriesIds, isPaid, rangeStart, rangeEnd, publishedState, pageable);
        }

        if (onlyAvailable && sort == EventSort.VIEWS) {
            return eventRepository.findOnlyAvailableByMultipleParametersOrderByViews(
                    lowerCaseText, categoriesIds, isPaid, rangeStart, rangeEnd, publishedState, pageable);
        }

        return Page.empty();
    }

    public Event findByIdPublicRequest(int eventId) {
        return eventRepository.findByIdAndState(eventId, PublicationState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event with id=%d was not found", eventId)));
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
            throw new InvalidConditionException(
                    String.format("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: %s",
                            eventDate));
        }
    }
}
