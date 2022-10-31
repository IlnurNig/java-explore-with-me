package ru.practicum.participationRequest.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.exceptionClass.ExceptionNotFound;
import ru.practicum.participationRequest.model.ParticipationRequest;
import ru.practicum.user.service.UserService;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ParticipationRequestMapper {

    @Autowired
    protected UserService userService;

    @Autowired
    protected EventService eventService;

    @Mapping(target = "event", expression = "java(eventService.getById(eventId))")
    @Mapping(target = "requester", expression = "java(userService.getById(requesterId))")
    public abstract ParticipationRequest toEntity(Long eventId, Long requesterId) throws ExceptionNotFound;

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    @Mapping(target = "status", source = "state")
    public abstract ParticipationRequestDto toDto(ParticipationRequest request);

    public abstract List<ParticipationRequestDto> toDto(List<ParticipationRequest> requests);

}
