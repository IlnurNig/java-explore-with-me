package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.StatsMapper;
import ru.practicum.dto.ViewStats;
import ru.practicum.model.Criteria;
import ru.practicum.model.Stats;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@Validated
@Slf4j
public class StatsController {
    private final StatsService statsService;

    private final StatsMapper statsMapper;

    @Autowired
    public StatsController(StatsService statsService, StatsMapper statsMapper) {
        this.statsService = statsService;
        this.statsMapper = statsMapper;
    }

    @GetMapping("/stats")
    public List<ViewStats> getViewStats(@RequestParam(name = "app", defaultValue = "ewm-main-service") String app,
                                        @RequestParam(name = "uris", required = false) List<String> uris,
                                        @RequestParam(name = "start", required = false)
                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                        @RequestParam(name = "end", required = false)
                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                        @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        log.info("getViewStats with app: {}, uris: {}, start: {}, end: {}, unique: {}",
                app, uris, start, end, unique);
        Criteria criteria = Criteria.builder()
                .uris(uris)
                .unique(unique)
                .start(start)
                .end(end)
                .app(app)
                .build();

        return statsService.getViewStats(criteria);
    }

    @PostMapping("/hit")
    public EndpointHit createStats(@RequestBody EndpointHit statsDto) {
        log.info("createStats: {}", statsDto);
        Stats stats = statsMapper.toEntity(statsDto);
        return statsMapper.toDto(statsService.createStats(stats));
    }
}
