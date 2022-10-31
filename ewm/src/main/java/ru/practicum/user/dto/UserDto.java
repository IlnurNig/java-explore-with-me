package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@Builder
public class UserDto {
    private long id;

    @Size(max = 255)
    @NotBlank
    private String name;

    @Size(max = 255)
    @NotBlank
    @Email
    private String email;
}
