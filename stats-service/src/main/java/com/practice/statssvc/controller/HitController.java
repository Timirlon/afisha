package com.practice.statssvc.controller;

import com.practice.statssvc.dto.EndpointHit;
import com.practice.statssvc.dto.ViewStats;
import com.practice.statssvc.mapper.HitMapper;
import com.practice.statssvc.model.Hit;
import com.practice.statssvc.service.HitService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;

import static com.practice.statssvc.util.DateTimeFormatConstants.parse;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RestController
public class HitController {
    HitService hitService;
    HitMapper hitMapper;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void hit(@RequestBody EndpointHit endpointHit) {
        Hit hit = hitMapper.fromDto(endpointHit);

        hitService.save(hit);
    }

    @GetMapping("/stats")
    public Collection<ViewStats> view(@RequestParam String start,
                                      @RequestParam String end,
                                      @RequestParam(required = false) String[] uris,
                                      @RequestParam(defaultValue = "false") boolean unique) {
        LocalDateTime parsedStart = parse(start);
        LocalDateTime parsedEnd = parse(end);

        return hitService.findAll(parsedStart, parsedEnd, uris, unique);
    }
}
