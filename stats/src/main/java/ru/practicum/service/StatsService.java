package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.ViewStats;
import ru.practicum.model.Criteria;
import ru.practicum.model.Stats;
import ru.practicum.repository.StatsCriteriaRepository;
import ru.practicum.repository.StatsRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatsService {
    private final StatsRepository statsRepository;

    private final StatsCriteriaRepository statsCriteriaRepository;

    @Autowired
    public StatsService(StatsRepository statsRepository, StatsCriteriaRepository statsCriteriaRepository) {
        this.statsRepository = statsRepository;
        this.statsCriteriaRepository = statsCriteriaRepository;
    }

    public Stats createStats(Stats stats) {
        log.info("createStats: {}", stats);
        return statsRepository.save(stats);
    }

    public List<ViewStats> getViewStats(Criteria criteria) {

        log.info("getViewStats with criteria: {}", criteria);

        List<Stats> stats = statsCriteriaRepository.findAllByCriteria(criteria);

        if (stats == null) {
            return new ArrayList<>();
        }

        if (criteria.getUnique()) {
            Set<String> elements = new HashSet<>();
            stats = stats.stream()
                    .filter(e -> elements.add(e.getApp() + e.getUri() + e.getIp()))
                    .collect(Collectors.toList());
        }

        Map<String, Long> countHits = new HashMap<>();
        stats.forEach(a -> countHits.put(a.getApp() + a.getUri(),
                countHits.containsKey(a.getApp() + a.getUri()) ? countHits.get(a.getApp() + a.getUri()) + 1 : 1));

        Set<String> elements = new HashSet<>();
        return stats.stream()
                .filter(e -> elements.add(e.getApp() + e.getUri()))
                .map(a -> ViewStats.builder()
                        .app(a.getApp())
                        .uri(a.getUri())
                        .hits(countHits.get(a.getApp() + a.getUri()))
                        .build())
                .collect(Collectors.toList());

    }


}
