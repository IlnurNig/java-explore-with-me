package ru.practicum.user.dto;

import org.mapstruct.Mapper;
import ru.practicum.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDto userDto);

    UserDto toDto(User user);

    List<UserDto> toDto(List<User> users);
}
