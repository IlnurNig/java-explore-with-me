package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.category.repository.CategoryAuditRepository;
import ru.practicum.category.service.CategoryService;

import java.util.List;

@SpringBootTest
//@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CategoryAuditTest {

    @Autowired
    private CategoryAuditRepository categoryAuditRepository;

    @Autowired
    private final CategoryService categoryService;


//    public ru.practicum.CategoryAuditTest(CategoryService categoryService) {
//        this.categoryService = categoryService;
//    }

    @Test
    void test(){
        List<Object> list = categoryAuditRepository.test();
        List<Object> list2 = categoryAuditRepository.testId(1L);
        System.out.println(list);
        System.out.println(list2);
        categoryService.containsId(1);
    }
}
