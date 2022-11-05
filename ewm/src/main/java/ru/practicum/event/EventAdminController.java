package ru.practicum.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Criteria;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.service.EventService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequestMapping("/admin/events")
public class EventAdminController {


    private final EventMapper eventMapper;

    private final EventService eventService;

    @Autowired
    public EventAdminController(EventMapper eventMapper, EventService eventService) {
        this.eventMapper = eventMapper;
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventFullDto> getAllByCriteria
            (@RequestParam(name = "users", required = false) List<Long> users,
             @RequestParam(name = "states", required = false) List<EventState> states,
             @RequestParam(name = "categories", required = false) List<Long> categories,
             @RequestParam(name = "rangeStart", required = false)
             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
             @RequestParam(name = "rangeEnd", required = false)
             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
             @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("getAllByCriteria with users: {}, states: {}, categories: {}, rangeStart: {}, rangeEnd: {}, " +
                        "from: {}, size: {}",
                users, states, categories, rangeStart, rangeEnd, from, size);

        Criteria criteria = Criteria.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();

        return eventMapper.toFullDto(eventService.getAllByCriteria(criteria));
    }


    @PatchMapping("{eventId}/publish")
    public EventFullDto setPublishState(@PathVariable @NotNull Long eventId) {
        log.info("setPublishState with eventId: {}", eventId);
        return eventMapper.toFullDto(eventService.setStateEvent(eventId, EventState.PUBLISHED));
    }

    @PatchMapping("{eventId}/reject")
    public EventFullDto setCanceledState(@PathVariable @NotNull Long eventId) {
        log.info("setCanceledState with eventId: {}", eventId);
        return eventMapper.toFullDto(eventService.setStateEvent(eventId, EventState.CANCELED));
    }

    @PutMapping("{eventId}")
    public EventFullDto updateEvent(@PathVariable @NotNull Long eventId,
                                    @RequestBody NewEventDto eventDto) {
        log.info("updateEvent with eventId: {}, eventDto: {}", eventId, eventDto);
        Event event = eventMapper.toEntity(eventDto);
        event.setId(eventId);
        return eventMapper.toFullDto(eventService.update(event));
    }

}
