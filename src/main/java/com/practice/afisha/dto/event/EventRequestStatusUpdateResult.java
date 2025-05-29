package com.practice.afisha.dto.event;

import com.practice.afisha.dto.request.ParticipationRequestDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateResult {
    final List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();

    final List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
}
