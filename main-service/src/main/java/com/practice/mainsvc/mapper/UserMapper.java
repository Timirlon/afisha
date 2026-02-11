package com.practice.mainsvc.mapper;

import com.practice.mainsvc.dto.user.NewUserRequest;
import com.practice.mainsvc.dto.user.UserDto;
import com.practice.mainsvc.dto.user.UserShortDto;
import com.practice.mainsvc.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public List<UserDto> toDto(Page<User> users) {
        return users.stream()
                .map(this::toDto)
                .toList();
    }

    public UserShortDto toShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public User fromDto(NewUserRequest userRequest) {
        return User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .build();
    }
}
