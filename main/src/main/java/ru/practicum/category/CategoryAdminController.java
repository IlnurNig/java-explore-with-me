package ru.practicum.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;
import ru.practicum.exception.exceptionClass.ExceptionNotFound;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@Validated
@Slf4j
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;

    @Autowired
    public CategoryAdminController(CategoryService categoryService, CategoryMapper mapper) {
        this.categoryService = categoryService;
        this.categoryMapper = mapper;
    }

    @PostMapping
    public CategoryDto create(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Create category {}", categoryDto);
        Category category = categoryMapper.toEntity(categoryDto);
        return categoryMapper.toDto(categoryService.create(category));
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable @NotNull Long id) throws ExceptionNotFound {
        log.info("delete category with id {}", id);
        categoryService.delete(id);
    }

    @PatchMapping
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto) throws ExceptionNotFound {
        log.info("Update category {}", categoryDto);
        Category category = categoryMapper.toEntity(categoryDto);
        return categoryMapper.toDto(categoryService.update(category));
    }

}
