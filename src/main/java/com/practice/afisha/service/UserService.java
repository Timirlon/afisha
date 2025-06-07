package com.practice.afisha.service;

import com.practice.afisha.exception.NotFoundException;
import com.practice.afisha.model.User;
import com.practice.afisha.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@Service
public class UserService {
    UserRepository userRepository;

    public Page<User> findAllByIdIn(Collection<Integer> ids, int from, int size) {
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);

        if (ids.isEmpty()) {
            return userRepository.findAll(pageable);
        }

        return userRepository.findAllByIdIn(ids, pageable);
    }

    public User save(User user) {
        userRepository.save(user);

        return user;
    }

    public void deleteById(int id) {
        userRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException(
                                String.format("User with id=%d was not found", id)));


        userRepository.deleteById(id);
    }
}
