package ru.practicum.statsClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.statsClient.dto.EndpointHit;
import ru.practicum.statsClient.dto.ViewStats;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public StatsClient(@Value("http://localhost:9090") String baseUrl,
                       RestTemplateBuilder builder,
                       ObjectMapper objectMapper) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(baseUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory.class)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .build();
        this.objectMapper = objectMapper;
    }

    public void postEndpointHit(HttpServletRequest request, String appName) {
        HttpEntity<EndpointHit> requestBody = new HttpEntity<>(
                EndpointHit.builder()
                        .uri(request.getRequestURI())
                        .ip(request.getRemoteAddr())
                        .app(appName)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
        String POST_PREFIX = "/hit";
        restTemplate.postForEntity(POST_PREFIX, requestBody, String.class);
    }

    public List<ViewStats> getViewStats(Map<String, String> param) throws Exception {
        String GET_PREFIX = "/stats";
        ResponseEntity<String> response = restTemplate.getForEntity(GET_PREFIX, String.class, param);
        if (response.getStatusCode().is2xxSuccessful()) {
            String body = response.getBody();
            return objectMapper.readValue(body, new TypeReference<List<ViewStats>>() {
            });
        }
        return new ArrayList<>();
    }

}
