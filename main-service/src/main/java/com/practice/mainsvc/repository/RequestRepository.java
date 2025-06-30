package com.practice.mainsvc.repository;

import com.practice.mainsvc.model.ParticipationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Integer> {
    List<ParticipationRequest> findAllByRequester_Id(int id);

    List<ParticipationRequest> findAllByEvent_IdAndEvent_Initiator_Id(int eventId, int initiatorId);

    List<ParticipationRequest> findAllByIdInAndEventId(Collection<Integer> ids, Integer eventId);
}
