package com.practice.afisha.controller.user;

import com.practice.afisha.dto.user.NewUserRequest;
import com.practice.afisha.dto.user.UserDto;
import com.practice.afisha.mapper.UserMapper;
import com.practice.afisha.model.User;
import com.practice.afisha.service.UserService;
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
    public List<UserDto> findAllByIdIn(@RequestParam Collection<Integer> ids,
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
