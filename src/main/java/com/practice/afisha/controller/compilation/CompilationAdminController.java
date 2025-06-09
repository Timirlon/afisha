package com.practice.afisha.controller.compilation;

import com.practice.afisha.dto.compilation.CompilationDto;
import com.practice.afisha.dto.compilation.NewCompilationDto;
import com.practice.afisha.dto.compilation.UpdateCompilationRequest;
import com.practice.afisha.mapper.CompilationMapper;
import com.practice.afisha.model.Compilation;
import com.practice.afisha.service.CompilationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RestController
@RequestMapping("/admin/compilations")
public class CompilationAdminController {
    CompilationService compilationService;
    CompilationMapper compilationMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createNew(@RequestBody @Valid NewCompilationDto compRequest) {
        Compilation compilation = compilationMapper.fromDto(compRequest);
        Collection<Integer> eventIds = compRequest.getEvents();

        return compilationMapper.toDto(
                compilationService.createNew(compilation, eventIds));
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable int compId) {
        compilationService.deleteById(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateById(@PathVariable int compId,
                                  @RequestBody @Valid UpdateCompilationRequest compRequest) {

        Compilation compilation = compilationMapper.fromDto(compRequest);
        Collection<Integer> eventIds = compRequest.getEvents();

        return compilationMapper.toDto(
                compilationService.updateById(compId, compilation, eventIds));
    }
}
