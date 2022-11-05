package ru.practicum.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class NewCommentDto {

    @NotBlank
    @Size(max = 2000)
    private String description;

    @Positive
    @NotNull
    private Long eventId;

}
