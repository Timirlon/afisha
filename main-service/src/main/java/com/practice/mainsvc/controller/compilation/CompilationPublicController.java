package com.practice.mainsvc.controller.compilation;

import com.practice.mainsvc.dto.compilation.CompilationDto;
import com.practice.mainsvc.mapper.CompilationMapper;
import com.practice.mainsvc.service.CompilationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RestController
@RequestMapping("/compilations")
public class CompilationPublicController {
    CompilationService compilationService;
    CompilationMapper compilationMapper;

    @GetMapping
    public List<CompilationDto> findAllByPinned(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {

        return compilationMapper.toDto(
                compilationService.findAllByPinned(pinned, from, size));
    }

    @GetMapping("/{compId}")
    public CompilationDto findById(@PathVariable int compId) {
        return compilationMapper.toDto(
                compilationService.findById(compId));
    }
}
