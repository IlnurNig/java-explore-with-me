package ru.practicum.comment.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.service.EventService;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.service.UserService;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {UserService.class, EventService.class, UserMapper.class, EventMapper.class}
)
public interface CommentMapper {

    @Mapping(target = "event", source = "newCommentDto.eventId")
    @Mapping(target = "commentator", source = "userId")
    Comment toEntity(NewCommentDto newCommentDto, Long userId);

    CommentDto toDto(Comment comment);

    List<CommentDto> toDto(List<Comment> comment);

}
