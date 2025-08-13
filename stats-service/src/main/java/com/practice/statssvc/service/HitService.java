package com.practice.statssvc.service;

import com.practice.statssvc.dto.ViewStats;
import com.practice.statssvc.model.Hit;
import com.practice.statssvc.repository.HitRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@Service
public class HitService {
    HitRepository hitRepository;

    public Hit save(Hit hit) {
        hitRepository.save(hit);

        return hit;
    }

    public Collection<ViewStats> findAll(LocalDateTime start, LocalDateTime end,
                                         String[] uris, boolean unique) {

        if ((uris == null || uris.length == 0)
                && !unique) {
            return hitRepository.findAllByCreatedInRange(start, end);
        }

        if (uris != null && uris.length > 0
                && !unique) {
            return hitRepository.findAllByCreatedInRangeAndUriIn(start, end, uris);
        }

        if ((uris == null || uris.length == 0)
                && unique) {
            return hitRepository.findAllByCreatedInRangeAndUniqueTrue(start, end);
        }

        if (uris.length > 0 && unique) {
            return hitRepository.findAllByCreatedInRangeAndUriInAndUniqueTrue(start, end, uris);
        }

        return List.of();
    }
}
