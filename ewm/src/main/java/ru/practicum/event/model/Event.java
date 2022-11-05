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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 2000)
    @Size(min = 20)
//    @Audited
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
//    @Audited(targetAuditMode = NOT_AUDITED)
    private Category category;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @Column(length = 2048)
//    @Audited
    private String description;

//    @Audited
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

//    @Audited
    private Double lat;

//    @Audited
    private Double lon;

//    @Audited
    private Boolean paid;

//    @Audited
    private Integer participantLimit;

    private LocalDateTime publishedOn;

//    @Audited
    private Boolean requestModeration;

//    @Audited
    @Enumerated(EnumType.STRING)
    private EventState state;

    @NotBlank
    @Size(max = 120)
    @Size(min = 3)
//    @Audited
    private String title;

    @OneToMany(mappedBy = "event",
            cascade = CascadeType.MERGE,
            fetch = FetchType.LAZY)
    @JsonIgnore
    Set<ParticipationRequest> requests = new HashSet<>();

    @ManyToMany(mappedBy = "events")
    Set<Compilation> compilations = new HashSet<>();

}
