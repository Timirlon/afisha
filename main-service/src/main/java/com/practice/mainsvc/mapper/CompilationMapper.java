package com.practice.mainsvc.mapper;

import com.practice.mainsvc.dto.compilation.CompilationDto;
import com.practice.mainsvc.dto.compilation.NewCompilationDto;
import com.practice.mainsvc.dto.compilation.UpdateCompilationRequest;
import com.practice.mainsvc.model.Compilation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@Component
public class CompilationMapper {
    EventMapper eventMapper;

    public CompilationDto toDto(Compilation compilation) {
        CompilationDto dto = new CompilationDto();

        dto.setId(compilation.getId());
        dto.setTitle(compilation.getTitle());
        dto.setPinned(compilation.getPinned());
        dto.setEvents(
                compilation.getEvents().stream()
                        .map(eventMapper::toShortDto)
                        .toList());


        return dto;
    }

    public List<CompilationDto> toDto(Page<Compilation> compilations) {
        return compilations.stream()
                .map(this::toDto)
                .toList();
    }

    public Compilation fromDto(NewCompilationDto dto) {
        Compilation compilation = new Compilation();

        compilation.setTitle(dto.getTitle());
        compilation.setPinned(dto.getPinned());

        return compilation;
    }

    public Compilation fromDto(UpdateCompilationRequest dto) {
        Compilation compilation = new Compilation();

        compilation.setTitle(dto.getTitle());
        compilation.setPinned(dto.getPinned());

        return compilation;
    }
}
