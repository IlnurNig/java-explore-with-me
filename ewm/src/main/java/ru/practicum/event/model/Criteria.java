package ru.practicum.event.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class Criteria {
    private List<Long> users;
    private List<EventState> states;
    private List<Long> categories;
    private Boolean paid;
    private java.time.LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private String text;
    private Integer from;
    private Integer size;
    private Boolean onlyAvailable;
}
