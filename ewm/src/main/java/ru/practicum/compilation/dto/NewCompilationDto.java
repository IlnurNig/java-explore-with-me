package ru.practicum.compilation.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class NewCompilationDto {

    @NotNull
    private Boolean pinned;

    @NotBlank
    @Size(max = 255)
    private String title;

    private List<Long> events;
}
