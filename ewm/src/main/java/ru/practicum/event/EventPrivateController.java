package ru.practicum.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.dto.ParticipationRequestMapper;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {

    private final EventService eventService;

    private final EventMapper eventMapper;

    private final ParticipationRequestMapper requestMapper;

    @Autowired
    public EventPrivateController(EventService eventService,
                                  EventMapper eventMapper,
                                  ParticipationRequestMapper requestMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.requestMapper = requestMapper;
    }


    @PostMapping
    public EventFullDto create(@PathVariable @NotNull Long userId,
                               @Valid @RequestBody NewEventDto eventDto) {
        log.info("create event with userId: {}, eventDto: {}", userId, eventDto);
        Event event = eventMapper.toEntity(eventDto, userId);
        return eventMapper.toFullDto(eventService.create(event));
    }

    @GetMapping
    public List<EventFullDto> getAllByInitiatorId(@PathVariable @NotNull Long userId,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                  Integer from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10")
                                                  Integer size) {
        log.info("getAllByInitiatorId with userId: {}, from: {}, size: {}", userId, from, size);
        return eventMapper.toFullDto(eventService.getAllByInitiatorId(userId, from, size));
    }

    @GetMapping("{eventId}")
    public EventFullDto getByIdAndInitiatorId(@PathVariable @NotNull Long userId,
                                              @PathVariable @NotNull Long eventId) {
        log.info("getByIdAndInitiatorId with userId: {}, eventId: {}", userId, eventId);
        return eventMapper.toFullDto(eventService.getByIdAndInitiatorId(userId, eventId));
    }

    @GetMapping("{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByUserIdAndEventId(@PathVariable @NotNull Long userId,
                                                                       @PathVariable @NotNull Long eventId) {
        log.info("get request by userId: {}, eventId: {}", userId, eventId);
        return requestMapper.toDto(eventService.getRequestsByUserIdAndEventId(userId, eventId));
    }

    @PatchMapping("{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirm(@PathVariable @NotNull Long reqId,
                                           @PathVariable @NotNull Long userId,
                                           @PathVariable @NotNull Long eventId) {
        log.info("confirm request with reqId: {}, userId: {}, eventId: {}", reqId, userId, eventId);
        return requestMapper.toDto(eventService.confirmRequest(reqId, userId, eventId));
    }

    @PatchMapping("{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto reject(@PathVariable @NotNull Long reqId,
                                          @PathVariable @NotNull Long userId,
                                          @PathVariable @NotNull Long eventId) {
        log.info("reject request with reqId: {}, userId: {}, eventId: {}", reqId, userId, eventId);
        return requestMapper.toDto(eventService.rejectRequest(reqId, userId, eventId));
    }

    @PatchMapping("{eventId}")
    public EventFullDto cancelEvent(@PathVariable @NotNull Long userId,
                                    @PathVariable @NotNull Long eventId) {
        log.info("cancel event with userId: {}, eventId: {}", userId, eventId);
        return eventMapper.toFullDto(eventService.cancelEvent(userId, eventId));
    }

    @PatchMapping
    public EventFullDto update(@PathVariable @NotNull Long userId,
                               @RequestBody NewEventDto eventDto) {
        log.info("update event with userId: {}, eventDto: {}", userId, eventDto);
        Event event = eventMapper.toEntity(eventDto, userId);
        return eventMapper.toFullDto(eventService.update(event));
    }

}
