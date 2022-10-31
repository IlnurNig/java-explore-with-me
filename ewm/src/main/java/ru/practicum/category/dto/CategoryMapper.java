package ru.practicum.category.dto;

import org.mapstruct.Mapper;
import ru.practicum.category.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryDto categoryDto);

    CategoryDto toDto(Category category);

    List<CategoryDto> toDto(List<Category> categories);

}
