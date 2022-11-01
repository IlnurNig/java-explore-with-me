package ru.practicum.event.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.category.model.Category;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.participationRequest.model.ParticipationRequest;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2048)
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @Column(length = 2048)
    private String description;

    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    private Double lat;

    private Double lon;

    private Boolean paid;

    private Integer participantLimit;

    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventState state;

    private String title;

    @OneToMany(mappedBy = "event",
            cascade = CascadeType.MERGE,
            fetch = FetchType.LAZY)
    @JsonIgnore
    Set<ParticipationRequest> requests = new HashSet<>();

    @ManyToMany(mappedBy = "events")
    Set<Compilation> compilations = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
