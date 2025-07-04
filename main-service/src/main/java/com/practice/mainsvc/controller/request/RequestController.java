package com.practice.mainsvc.controller.request;

import com.practice.mainsvc.client.StatisticsClient;
import com.practice.mainsvc.dto.request.ParticipationRequestDto;
import com.practice.mainsvc.mapper.RequestMapper;
import com.practice.mainsvc.service.RequestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RestController
@RequestMapping("/users/{userId}/requests")
public class RequestController {
    RequestService requestService;
    RequestMapper requestMapper;

    StatisticsClient statisticsClient;

    @GetMapping
    public List<ParticipationRequestDto> findAllByRequesterId(@PathVariable int userId,
                                                              HttpServletRequest servletRequest) {
        List<ParticipationRequestDto> result = requestMapper.toDto(
                requestService.findAllByRequesterId(userId));

        statisticsClient.hit(String.format(
                "/users/%d/requests", userId), servletRequest);

        return result;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createNew(@PathVariable int userId,
                                             @RequestParam int eventId,
                                             HttpServletRequest servletRequest) {
        ParticipationRequestDto result = requestMapper.toDto(
                requestService.createNew(userId, eventId));

        statisticsClient.hit(
                String.format("/users/%d/requests", userId), servletRequest);

        return result;
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable int requestId,
                                          @PathVariable int userId,
                                          HttpServletRequest servletRequest) {
        ParticipationRequestDto result = requestMapper.toDto(
                requestService.cancel(requestId, userId));

        statisticsClient.hit(
                String.format("/users/%d/requests/%d/cancel", userId, requestId),
                servletRequest);

        return result;
    }
}
