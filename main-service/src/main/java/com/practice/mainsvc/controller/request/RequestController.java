package com.practice.mainsvc.controller.request;

import com.practice.mainsvc.dto.request.ParticipationRequestDto;
import com.practice.mainsvc.mapper.RequestMapper;
import com.practice.mainsvc.service.RequestService;
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

    @GetMapping
    public List<ParticipationRequestDto> findAllByRequesterId(@PathVariable int userId) {
        return requestMapper.toDto(
                requestService.findAllByRequesterId(userId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createNew(@PathVariable int userId,
                                             @RequestParam int eventId) {
        return requestMapper.toDto(
                requestService.createNew(userId, eventId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable int requestId,
                                          @PathVariable int userId) {

        return requestMapper.toDto(
                requestService.cancel(requestId, userId));
    }
}
