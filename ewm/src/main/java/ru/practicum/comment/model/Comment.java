package ru.practicum.comment.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 2000)
    @Audited
    private String description;

    @CreationTimestamp
    @Audited
    private LocalDateTime publishedOn;

    @Audited
    private LocalDateTime updateOn;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @Audited(targetAuditMode = NOT_AUDITED)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "commentator_id")
    @Audited(targetAuditMode = NOT_AUDITED)
    private User commentator;

}