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
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    public List<UserDto> toDto(Page<User> users) {
        return users.stream()
                .map(this::toDto)
                .toList();
    }

    public User fromDto(NewUserRequest userRequest) {
        User user = new User();

        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());

        return user;
    }

    public UserShortDto toShortDto(User user) {
        UserShortDto userDto = new UserShortDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());

        return userDto;
    }
}
