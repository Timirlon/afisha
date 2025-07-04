package com.practice.mainsvc.controller.compilation;

import com.practice.mainsvc.client.StatisticsClient;
import com.practice.mainsvc.dto.compilation.CompilationDto;
import com.practice.mainsvc.dto.compilation.NewCompilationDto;
import com.practice.mainsvc.dto.compilation.UpdateCompilationRequest;
import com.practice.mainsvc.mapper.CompilationMapper;
import com.practice.mainsvc.model.Compilation;
import com.practice.mainsvc.service.CompilationService;
import jakarta.servlet.http.HttpServletRequest;
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

    StatisticsClient statisticsClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createNew(@RequestBody @Valid NewCompilationDto compRequest,
                                    HttpServletRequest httpServletRequest) {
        Compilation compilation = compilationMapper.fromDto(compRequest);
        Collection<Integer> eventIds = compRequest.getEvents();

        CompilationDto result = compilationMapper.toDto(
                compilationService.createNew(compilation, eventIds));

        statisticsClient.hit(
                "/admin/compilations", httpServletRequest);


        return result;
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable int compId,
                           HttpServletRequest httpServletRequest) {
        compilationService.deleteById(compId);

        statisticsClient.hit(
                String.format("/admin/compilations/%d", compId), httpServletRequest);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateById(@PathVariable int compId,
                                     @RequestBody @Valid UpdateCompilationRequest compRequest,
                                     HttpServletRequest httpServletRequest) {

        Compilation compilation = compilationMapper.fromDto(compRequest);
        Collection<Integer> eventIds = compRequest.getEvents();

        CompilationDto result = compilationMapper.toDto(
                compilationService.updateById(compId, compilation, eventIds));

        statisticsClient.hit(
                String.format("/admin/compilations/%d", compId), httpServletRequest);

        return result;
    }
}
