package com.practice.afisha.mapper;

import com.practice.afisha.dto.request.ParticipationRequestDto;
import com.practice.afisha.model.ParticipationRequest;
import org.springframework.stereotype.Component;

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
}
