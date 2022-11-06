package ru.practicum.category.service;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.exceptionClass.ExceptionNotFound;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Service
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private final AuditReader auditReader;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, AuditReader auditReader) {
        this.categoryRepository = categoryRepository;
        this.auditReader = auditReader;
    }

    public Category create(Category category) {
        log.info("Create category {}", category);
        return categoryRepository.save(category);
    }

    public Category update(Category category) {
        log.info("Update category {}", category);
        validateId(category.getId());
        return categoryRepository.save(category);
    }

    public List<Category> getAll(Integer from, Integer size) {
        log.info("getAll category with from {}, size {}", from, size);
        Pageable pageable = PageRequest.of(
                from / size,
                size);
        return categoryRepository.findAll(pageable).getContent();
    }

    public Category getById(Long id) {
        log.info("getById category with id {}", id);
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ExceptionNotFound(String.format("Category with Id=%d is missing", id)));
    }

    public void delete(Long id) {
        log.info("delete category with id {}", id);
        validateId(id);
        categoryRepository.deleteById(id);
    }

    private void validateId(long id) {
        if (containsId(id)) {
            log.debug("Category with Id={} is missing", id);
            throw new ExceptionNotFound(String.format("Category with Id=%d is missing", id));
        }
    }

    public boolean containsId(long id) {
        log.info("check contains category id {}", id);
        return categoryRepository.findById(id).isEmpty();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List getAuditById(@Positive Long id,
                             @PositiveOrZero Integer from,
                             @Positive Integer size) {
        return auditReader
                .createQuery()
                .forRevisionsOfEntity(Category.class, false, true)
                .add(AuditEntity.id().eq(id))
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }

}
