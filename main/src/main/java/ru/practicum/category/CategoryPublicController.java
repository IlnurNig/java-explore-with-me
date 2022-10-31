package ru.practicum.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.service.CategoryService;
import ru.practicum.exception.exceptionClass.ExceptionNotFound;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@Validated
@Slf4j
@RequestMapping("/categories")
public class CategoryPublicController {
    private final CategoryService categoryService;

    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryPublicController(CategoryService categoryService, CategoryMapper mapper) {
        this.categoryService = categoryService;
        this.categoryMapper = mapper;
    }

    @GetMapping("{catId}")
    public CategoryDto getById(@PathVariable @NotNull Long catId) throws ExceptionNotFound {
        log.info("getById category with id {}", catId);
        return categoryMapper.toDto(categoryService.getById(catId));
    }

    @GetMapping
    public List<CategoryDto> getAll(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                    @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("getAll category with from {}, size {}", from, size);
        return categoryMapper.toDto(categoryService.getAll(from, size));
    }

}
