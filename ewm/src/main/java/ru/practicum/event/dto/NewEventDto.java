package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.event.model.Location;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
public class NewEventDto {

    private Long eventId;

    @NotBlank
    @Size(max = 2000)
    @Size(min = 20)
    private String annotation;

    @NotNull
    private Long category;

    @NotBlank
    @Size(max = 7000)
    @Size(min = 20)
    private String description;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Location location;

    @NotNull
    private Boolean paid;

    @NotNull
    @Min(value = 0L)
    private Integer participantLimit;

    @NotNull
    private Boolean requestModeration = true;

    @NotBlank
    @Size(max = 120)
    @Size(min = 3)
    private String title;

}
