package ru.practicum.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Criteria;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.Sort;
import ru.practicum.event.service.EventService;
import ru.practicum.statsClient.StatsClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequestMapping("/events")
public class EventPublicController {

    private final EventService eventService;

    private final EventMapper eventMapper;

    private final StatsClient statsClient;

    @Autowired
    public EventPublicController(EventService eventService, EventMapper eventMapper, StatsClient statsClient) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.statsClient = statsClient;
    }

    @GetMapping("{eventId}")
    public EventFullDto getPublishedEventById(@PathVariable @NotNull Long eventId,
                                              HttpServletRequest request) {
        log.info("getPublishedEventById  with eventId: {}, request: {}", eventId, request);
        statsClient.postEndpointHit(request, "ewm-main-service");
        return eventMapper.toFullDto(eventService.getByIdAndState(eventId, EventState.PUBLISHED));
    }

    @GetMapping
    public List<EventShortDto> getAllByCriteria
            (@RequestParam(name = "text", required = false) String text,
             @RequestParam(name = "categories", required = false) List<Long> categories,
             @RequestParam(name = "paid", required = false) Boolean paid,
             @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
             @RequestParam(name = "sort", required = false) Sort sort,
             @RequestParam(name = "rangeStart", required = false)
             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
             @RequestParam(name = "rangeEnd", required = false)
             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
             @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
             HttpServletRequest request) {

        log.info("getAllByCriteria with text: {}, categories: {}, paid: {}, onlyAvailable: {}, sort: {}, " +
                        "rangeStart: {}, rangeEnd: {}, from: {}, size: {}",
                text, categories, paid, onlyAvailable, sort, rangeStart, rangeEnd, from, size);

        Criteria criteria = Criteria.builder()
                .text(text)
                .paid(paid)
                .onlyAvailable(onlyAvailable)
                .states(List.of(EventState.PUBLISHED))
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();

        statsClient.postEndpointHit(request, "ewm-main-service");
        return eventMapper.toShortDto(eventService.getAllByCriteria(criteria), sort);
    }
}
