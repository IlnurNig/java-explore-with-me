package ru.practicum.event.dto;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Sort;
import ru.practicum.exception.exceptionClass.ExceptionNotFound;
import ru.practicum.participationRequest.model.RequestState;
import ru.practicum.statsClient.StatsClient;
import ru.practicum.user.service.UserService;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class EventMapper {

    @Autowired
    protected UserService userService;

    @Autowired
    protected CategoryService categoryService;

    @Autowired
    protected CategoryMapper categoryMapper;

    @Autowired
    protected StatsClient statsClient;


    @Mapping(target = "initiator", expression = "java(userService.getById(userId))")
    @Mapping(target = "id", source = "eventDto.eventId")
    @Mapping(target = "lat", source = "eventDto.location.lat")
    @Mapping(target = "lon", source = "eventDto.location.lon")
    @Mapping(target = "category", expression = "java(categoryService.getById(eventDto.getCategory()))")
    public abstract Event toEntity(NewEventDto eventDto, Long userId) throws ExceptionNotFound;

    @Mapping(target = "id", source = "eventDto.eventId")
    @Mapping(target = "lat", source = "eventDto.location.lat")
    @Mapping(target = "lon", source = "eventDto.location.lon")
    @Mapping(target = "category", expression = "java(categoryService.getById(eventDto.getCategory()))")
    public abstract Event toEntity(NewEventDto eventDto) throws ExceptionNotFound;

    @Mapping(target = "location.lat", source = "event.lat")
    @Mapping(target = "location.lon", source = "event.lon")
    @Mapping(target = "views", expression = "java(getViews(event))")
    @Mapping(target = "category", expression = "java(categoryMapper.toDto(event.getCategory()))")
    @Mapping(target = "confirmedRequests", expression = "java(countConfirmedRequests(event))")
    public abstract EventFullDto toFullDto(Event event);

    public abstract List<EventFullDto> toFullDto(List<Event> events);

    @Mapping(target = "views", expression = "java(getViews(event))")
    @Mapping(target = "category", expression = "java(categoryMapper.toDto(event.getCategory()))")
    @Mapping(target = "confirmedRequests", expression = "java(countConfirmedRequests(event))")
    public abstract EventShortDto toShortDto(Event event);

    public abstract List<EventShortDto> toShortDto(List<Event> events);

    public List<EventShortDto> toShortDto(List<Event> events, Sort sort) {
        List<EventShortDto> eventShortDtoList = toShortDto(events);
        if (sort == Sort.EVENT_DATE) {
            return eventShortDtoList.stream().sorted(Comparator.comparing(EventShortDto::getEventDate))
                    .collect(Collectors.toList());
        } else if (sort == Sort.VIEWS) {
            return eventShortDtoList.stream().sorted(Comparator.comparing(EventShortDto::getViews))
                    .collect(Collectors.toList());
        } else {
            return eventShortDtoList;
        }
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEventIgnoreNull(Event newEvent, @MappingTarget Event updatedEvent);

    protected long countConfirmedRequests(Event event) {
        if (event.getRequests() != null) {
            return event.getRequests().stream().filter(r -> r.getState() == RequestState.CONFIRMED).count();
        }
        return 0L;
    }

    protected long getViews(Event event) {
        Map<String, String> param = new HashMap<>();
        String uris = "/events/" + event.getId();
        param.put("uris", uris);
        try {
            return statsClient.getViewStats(param).size();
        } catch (Exception ex) {
            return 0;
        }
    }

}
