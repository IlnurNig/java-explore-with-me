package ru.practicum.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Builder
public class ViewStats {
    private String app;
    private String uri;

    private Long hits;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewStats viewStats = (ViewStats) o;
        return Objects.equals(app, viewStats.app) && Objects.equals(uri, viewStats.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, uri);
    }
}
