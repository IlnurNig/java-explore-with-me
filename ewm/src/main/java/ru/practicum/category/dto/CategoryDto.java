package ru.practicum.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
public class CategoryDto {

    private Long id;

    @NotBlank
    @Size(max = 255)
    private String name;

}
