package com.practice.mainsvc.controller.user;

import com.practice.mainsvc.client.StatisticsClient;
import com.practice.mainsvc.dto.user.NewUserRequest;
import com.practice.mainsvc.dto.user.UserDto;
import com.practice.mainsvc.mapper.UserMapper;
import com.practice.mainsvc.model.User;
import com.practice.mainsvc.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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

    StatisticsClient statisticsClient;

    @GetMapping
    public List<UserDto> findAllByIdIn(@RequestParam(required = false) Collection<Integer> ids,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size,
                                       HttpServletRequest servletRequest) {

        List<UserDto> result = userMapper.toDto(
                userService.findAllByIdIn(ids, from, size));

        statisticsClient.hit("/admin/users", servletRequest);

        return result;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto save(@RequestBody @Valid NewUserRequest userRequest,
                        HttpServletRequest servletRequest) {
        User user = userMapper.fromDto(userRequest);

        UserDto result = userMapper.toDto(
                userService.save(user));

        statisticsClient.hit("/admin/users", servletRequest);

        return result;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable int userId,
                           HttpServletRequest servletRequest) {
        userService.deleteById(userId);

        statisticsClient.hit(String.format("/admin/users/%d", userId), servletRequest);
    }
}
