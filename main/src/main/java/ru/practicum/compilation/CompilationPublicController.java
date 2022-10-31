package ru.practicum.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationMapper;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.exception.exceptionClass.ExceptionNotFound;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequestMapping("/compilations")
public class CompilationPublicController {
    private final CompilationMapper compilationMapper;

    private final CompilationService compilationService;

    @Autowired
    public CompilationPublicController(CompilationMapper mapper, CompilationService compilationService) {
        this.compilationMapper = mapper;
        this.compilationService = compilationService;
    }

    @GetMapping("{id}")
    public CompilationDto get(@PathVariable @NotNull Long id) throws ExceptionNotFound {
        log.info("get compilation with id: {}", id);
        return compilationMapper.toDto(compilationService.getById(id));
    }

    @GetMapping
    public List<CompilationDto> getAll(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                       @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                       @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("get all compilation with pinned: {}, from: {}, size: {}", pinned, from, size);
        return compilationMapper.toDto(compilationService.getAll(pinned, from, size));
    }

}
