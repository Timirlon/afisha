package com.practice.afisha.repository;

import com.practice.afisha.model.ParticipationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Integer> {
    List<ParticipationRequest> findAllByRequester_Id(int id);
}
