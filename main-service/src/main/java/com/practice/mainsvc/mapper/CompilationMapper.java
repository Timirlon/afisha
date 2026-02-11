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
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents().stream()
                        .map(eventMapper::toShortDto)
                        .toList())
                .build();
    }

    public List<CompilationDto> toDto(Page<Compilation> compilations) {
        return compilations.stream()
                .map(this::toDto)
                .toList();
    }

    public Compilation fromDto(NewCompilationDto dto) {
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned())
                .build();
    }

    public Compilation fromDto(UpdateCompilationRequest dto) {
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned())
                .build();
    }
}
