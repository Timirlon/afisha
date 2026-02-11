package com.practice.mainsvc.mapper;

import com.practice.mainsvc.dto.request.EventRequestStatusUpdateResult;
import com.practice.mainsvc.dto.request.ParticipationRequestDto;
import com.practice.mainsvc.model.ConfirmationStatus;
import com.practice.mainsvc.model.ParticipationRequest;
import com.practice.mainsvc.util.DateTimeFormatConstants;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RequestMapper {

    public ParticipationRequestDto toDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(DateTimeFormatConstants.format(
                        request.getCreated()))
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus().name())
                .build();
    }

    public List<ParticipationRequestDto> toDto(List<ParticipationRequest> requests) {
        return requests.stream()
                .map(this::toDto)
                .toList();
    }

    public EventRequestStatusUpdateResult toUpdDto(List<ParticipationRequest> requests) {

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        requests.stream()
                .map(this::toDto)
                .forEach(request -> {
                    if (request.getStatus().equals(
                            ConfirmationStatus.CONFIRMED.name())) {
                        confirmedRequests.add(request);
                        return;
                    }

                    if (request.getStatus().equals(
                            ConfirmationStatus.REJECTED.name())) {
                        rejectedRequests.add(request);
                    }
                });


        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }
}
