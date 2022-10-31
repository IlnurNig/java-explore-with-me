package ru.practicum.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.exceptionClass.ExceptionConflict;
import ru.practicum.exception.exceptionClass.ExceptionNotFound;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequestMapping(path = "/admin/users")
public class UserController {
    private final UserService userService;

    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) throws ExceptionConflict {
        log.info("createUser: {}", userDto);
        User user = userMapper.toEntity(userDto);
        return userMapper.toDto(userService.createUser(user));
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable @NotNull Long id) throws ExceptionNotFound {
        log.info("deleteUser with id: {}", id);
        userService.deleteUser(id);
    }

    @GetMapping
    public List<UserDto> get(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                             @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                             @RequestParam(name = "ids", required = false) List<Long> listId) {
        return userMapper.toDto(userService.getAllByListId(from, size, listId));
    }

}
