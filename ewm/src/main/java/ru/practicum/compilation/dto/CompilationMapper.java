package ru.practicum.compilation.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.service.EventService;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CompilationMapper {

    @Autowired
    protected EventMapper eventMapper;

    @Autowired
    protected EventService eventService;

    @Mapping(target = "events", expression = "java(eventMapper.toShortDto(new ArrayList<>(compilation.getEvents())))")
    public abstract CompilationDto toDto(Compilation compilation);

    public abstract List<CompilationDto> toDto(List<Compilation> compilations);

    @Mapping(target = "events", expression = "java(eventService.getByIdx(newCompilationDto.getEvents()))")
    public abstract Compilation toEntity(NewCompilationDto newCompilationDto);

}
