package ru.practicum.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationMapper;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@Validated
@Slf4j
@RequestMapping("/admin/compilations")
public class CompilationAdminController {

    private final CompilationMapper compilationMapper;

    private final CompilationService compilationService;

    @Autowired
    public CompilationAdminController(CompilationMapper mapper, CompilationService compilationService) {
        this.compilationMapper = mapper;
        this.compilationService = compilationService;
    }

    @PostMapping
    public CompilationDto create(@Valid @RequestBody NewCompilationDto compilationDto) {
        log.info("Create compilation: {}", compilationDto);
        Compilation compilation = compilationMapper.toEntity(compilationDto);
        return compilationMapper.toDto(compilationService.create(compilation));
    }

    @DeleteMapping("{compId}")
    public void delete(@PathVariable @NotNull Long compId) {
        log.info("delete compilation with id: {}", compId);
        compilationService.delete(compId);
    }

    @PatchMapping("{compId}/events/{eventId}")
    public void addEvent(@PathVariable @NotNull Long eventId,
                         @PathVariable @NotNull Long compId) {
        log.info("add event id {} compilation with id: {}", eventId, compId);
        compilationService.addEvent(eventId, compId);
    }

    @DeleteMapping("{compId}/events/{eventId}")
    public void deleteEvent(@PathVariable @NotNull Long eventId,
                            @PathVariable @NotNull Long compId) {
        log.info("delete event id {} compilation with id: {}", eventId, compId);
        compilationService.deleteEvent(eventId, compId);
    }

    @DeleteMapping("{compId}/pin")
    public void unpin(@PathVariable @NotNull Long compId) {
        log.info("unpin compilation with id: {}", compId);
        compilationService.pinCompilation(compId, false);
    }

    @PatchMapping("{compId}/pin")
    public void pin(@PathVariable @NotNull Long compId) {
        log.info("pin compilation with id: {}", compId);
        compilationService.pinCompilation(compId, true);
    }

}
