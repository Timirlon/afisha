package com.practice.mainsvc.controller.user;

import com.practice.mainsvc.dto.user.NewUserRequest;
import com.practice.mainsvc.dto.user.UserDto;
import com.practice.mainsvc.mapper.UserMapper;
import com.practice.mainsvc.model.User;
import com.practice.mainsvc.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RestController
@RequestMapping("/admin/users")
public class UserController {
    UserService userService;
    UserMapper userMapper;

    @GetMapping
    public List<UserDto> findAllByIdIn(@RequestParam(required = false) Collection<Integer> ids,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {

        return userMapper.toDto(
                userService.findAllByIdIn(ids, from, size));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto save(@RequestBody @Valid NewUserRequest userRequest) {
        User user = userMapper.fromDto(userRequest);

        return userMapper.toDto(
                userService.save(user));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable int userId) {
        userService.deleteById(userId);
    }
}
