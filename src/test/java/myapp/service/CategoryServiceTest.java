package myapp.service;

import myapp.dao.CategoryDAO;
import myapp.model.Category;
import myapp.repo.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import myapp.dao.impl.CategoryDAOImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests JUnit pour le service CategoryService.
 */
@DataJpaTest
@Import({CategoryDAOImpl.class, CategoryService.class})
public class CategoryServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testCreateCategory() {
        Category created = categoryService.createCategory("Escalade Sportive");
        assertNotNull(created.getId());
        assertEquals("Escalade Sportive", created.getName());
    }

    @Test
    public void testCreateCategoryWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory("   "));
    }

    @Test
    public void testGetAllCategories() {
        categoryService.createCategory("Escalade Sportive");
        categoryService.createCategory("Alpinisme");

        List<Category> categories = categoryService.getAllCategories();
        assertTrue(categories.size() >= 2);
    }

    @Test
    public void testUpdateCategory() {
        Category created = categoryService.createCategory("Escalade Sportive");
        Category updated = categoryService.updateCategory(created.getId(), "Alpinisme");

        assertEquals("Alpinisme", updated.getName());
    }

    @Test
    public void testUpdateNonExistentCategory() {
        assertThrows(IllegalArgumentException.class, 
            () -> categoryService.updateCategory(999L, "NewName"));
    }

    @Test
    public void testDeleteCategory() {
        Category created = categoryService.createCategory("Escalade Sportive");
        categoryService.deleteCategory(created.getId());

        List<Category> categories = categoryService.getAllCategories();
        assertTrue(categories.isEmpty());
    }

    @Test
    public void testGetTotalCategories() {
        categoryService.createCategory("Escalade Sportive");
        long count = categoryService.getTotalCategories();
        assertTrue(count >= 1);
    }
}

