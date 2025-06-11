package com.practice.afisha.mapper;

import com.practice.afisha.dto.request.EventRequestStatusUpdateResult;
import com.practice.afisha.dto.request.ParticipationRequestDto;
import com.practice.afisha.model.ConfirmationStatus;
import com.practice.afisha.model.ParticipationRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.practice.afisha.util.DateTimeFormatConstants.formatToString;

@Component
public class RequestMapper {

    public ParticipationRequestDto toDto(ParticipationRequest request) {
        ParticipationRequestDto dto = new ParticipationRequestDto();

        dto.setCreated(
                formatToString(request.getCreated()));

        dto.setEvent(request.getEvent().getId());
        dto.setId(request.getId());
        dto.setRequester(request.getRequester().getId());
        dto.setStatus(request.getStatus().name());


        return dto;
    }

    public List<ParticipationRequestDto> toDto(List<ParticipationRequest> requests) {
        return requests.stream()
                .map(this::toDto)
                .toList();
    }

    public EventRequestStatusUpdateResult toUpdDto(List<ParticipationRequest> requests) {

        EventRequestStatusUpdateResult dto = new EventRequestStatusUpdateResult();

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

        dto.setConfirmedRequests(confirmedRequests);
        dto.setRejectedRequests(rejectedRequests);

        return dto;
    }
}
