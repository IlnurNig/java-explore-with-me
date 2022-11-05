package ru.practicum.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class Criteria {
    private List<String> uris;
    private Boolean unique;
    private LocalDateTime start;
    private LocalDateTime end;
    private String app;
}
