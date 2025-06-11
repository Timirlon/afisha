package com.practice.afisha.service;

import com.practice.afisha.exception.NotFoundException;
import com.practice.afisha.model.Compilation;
import com.practice.afisha.model.Event;
import com.practice.afisha.repository.CompilationRepository;
import com.practice.afisha.repository.EventRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@Service
public class CompilationService {
    CompilationRepository compilationRepository;
    EventRepository eventRepository;

    public Compilation createNew(Compilation compilation, Collection<Integer> eventIds) {
        List<Event> events = eventRepository.findAllByIdIn(eventIds);

        compilation.setEvents(events);

        if (compilation.getPinned() == null) {
            compilation.setPinned(false);
        }

        compilationRepository.save(compilation);


        return compilation;
    }

    public void deleteById(int id) {
        findByIdOrElseThrow(id);

        compilationRepository.deleteById(id);
    }

    public Compilation updateById(int id, Compilation compilation, Collection<Integer> eventIds) {
        Compilation foundCompilation = findByIdOrElseThrow(id);

        List<Event> events = null;
        if (eventIds != null && !eventIds.isEmpty()) {
            events = eventRepository.findAllByIdIn(eventIds);
        }

        if (events != null  && !events.isEmpty()) {
            foundCompilation.setEvents(events);
        }

        if (compilation.getTitle() != null && !compilation.getTitle().isBlank()) {
            foundCompilation.setTitle(compilation.getTitle());
        }

        if (compilation.getPinned() != null) {
            foundCompilation.setPinned(compilation.getPinned());
        }

        compilationRepository.save(foundCompilation);


        return foundCompilation;
    }

    public Page<Compilation> findAllByPinned(Boolean pinned, int from, int size) {
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);

        if (pinned == null) {
            return compilationRepository.findAll(pageable);
        }

        return compilationRepository.findAllByPinned(pinned, pageable);
    }

    public Compilation findById(int id) {
        return findByIdOrElseThrow(id);
    }

    private Compilation findByIdOrElseThrow(int compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Compilation with id=%d was not found", compId)));
    }
}
