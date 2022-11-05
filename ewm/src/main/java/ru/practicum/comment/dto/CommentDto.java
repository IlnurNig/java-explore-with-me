package ru.practicum.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CommentDto {
    private Long id;

    private String description;

    private LocalDateTime publishedOn;

    private LocalDateTime updateOn;

    private EventShortDto event;

    private UserShortDto commentator;
}
