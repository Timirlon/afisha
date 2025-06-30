package com.practice.statssvc.controller;

import com.practice.statssvc.dto.EndpointHit;
import com.practice.statssvc.dto.ViewStats;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class StatsController {

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void hit(@RequestBody EndpointHit endpointHit) {

    }

    @GetMapping("/stats")
    public Collection<ViewStats> view(@RequestParam String start,
                                      @RequestParam String end,
                                      @RequestParam(required = false) String[] uris,
                                      @RequestParam(defaultValue = "false") boolean unique) {
        return null;
    }
}
