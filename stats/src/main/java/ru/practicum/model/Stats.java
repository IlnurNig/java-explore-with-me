package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "stats")
public class Stats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String app;

    @NotBlank
    @Size(max = 255)
    private String uri;

    @NotBlank
    private String ip;

    @CreationTimestamp
    private LocalDateTime createdOn;
}
