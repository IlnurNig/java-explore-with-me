package ru.practicum.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.model.Stats;

@Mapper(componentModel = "spring")
public interface StatsMapper {

    @Mapping(target = "timestamp", source = "createdOn")
    EndpointHit toDto(Stats stats);

    @Mapping(target = "createdOn", source = "timestamp")
    Stats toEntity(EndpointHit statsDto);

}
