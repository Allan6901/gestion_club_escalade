package myapp.dao;

import myapp.model.Category;
import myapp.model.Member;
import myapp.model.Trip;
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

@DataJpaTest
@Import(CategoryDAOImpl.class)
public class CategoryDAOTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryDAO categoryDAO;

    private Category testCategory;

    @BeforeEach
    public void setUp() {
        testCategory = new Category();
        testCategory.setName("Escalade Sportive");
    }

    @Test
    public void testCreateCategory() {
        Category created = categoryDAO.createCategory(testCategory);
        assertNotNull(created.getId());
        assertEquals("Escalade Sportive", created.getName());
    }

    @Test
    public void testCreateCategoryWithNullName() {
        testCategory.setName(null);
        assertThrows(IllegalArgumentException.class, () -> categoryDAO.createCategory(testCategory));
    }

    @Test
    public void testCreateCategoryWithEmptyName() {
        testCategory.setName("   ");
        assertThrows(IllegalArgumentException.class, () -> categoryDAO.createCategory(testCategory));
    }

    @Test
    public void testCreateNullCategory() {
        assertThrows(IllegalArgumentException.class, () -> categoryDAO.createCategory(null));
    }

    @Test
    public void testGetAllCategories() {
        Category cat1 = new Category();
        cat1.setName("Catégorie 1");
        Category cat2 = new Category();
        cat2.setName("Catégorie 2");

        entityManager.persistAndFlush(cat1);
        entityManager.persistAndFlush(cat2);

        List<Category> categories = categoryDAO.getAllCategories();
        assertTrue(categories.size() >= 2);
    }

    @Test
    public void testGetCategoryById() {
        Category saved = entityManager.persistAndFlush(testCategory);
        Optional<Category> found = categoryDAO.getCategoryById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Escalade Sportive", found.get().getName());
    }

    @Test
    public void testGetCategoryByIdNotFound() {
        Optional<Category> found = categoryDAO.getCategoryById(999L);
        assertFalse(found.isPresent());
    }

    @Test
    public void testGetCategoryByIdInvalid() {
        Optional<Category> found = categoryDAO.getCategoryById(null);
        assertFalse(found.isPresent());

        found = categoryDAO.getCategoryById(-1L);
        assertFalse(found.isPresent());
    }

    @Test
    public void testGetCategoryWithTrips() {
        Category saved = entityManager.persistAndFlush(testCategory);

        Member member = new Member();
        member.setLastName("Dupont");
        member.setFirstName("Jean");
        member.setEmail("jean@example.com");
        member.setPassword("pass123");
        entityManager.persistAndFlush(member);

        Trip trip = new Trip();
        trip.setName("Sortie 1");
        trip.setCategory(saved);
        trip.setCreator(member);
        entityManager.persistAndFlush(trip);

        entityManager.flush();
        entityManager.clear();

        Optional<Category> found = categoryDAO.getCategoryWithTrips(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(1, found.get().getTrips().size());
    }

    @Test
    public void testUpdateCategory() {
        Category saved = entityManager.persistAndFlush(testCategory);
        saved.setName("Alpinisme");

        Category updated = categoryDAO.updateCategory(saved);
        assertEquals("Alpinisme", updated.getName());
    }

    @Test
    public void testUpdateNonExistentCategory() {
        testCategory.setId(999L);
        assertThrows(IllegalArgumentException.class, () -> categoryDAO.updateCategory(testCategory));
    }

    @Test
    public void testDeleteCategory() {
        Category saved = entityManager.persistAndFlush(testCategory);
        categoryDAO.deleteCategory(saved.getId());

        assertFalse(categoryDAO.categoryExists(saved.getId()));
    }

    @Test
    public void testDeleteNonExistentCategory() {
        assertThrows(IllegalArgumentException.class, () -> categoryDAO.deleteCategory(999L));
    }

    @Test
    public void testGetTripsByCategory() {
        Category saved = entityManager.persistAndFlush(testCategory);

        Member member = new Member();
        member.setLastName("Martin");
        member.setFirstName("Paul");
        member.setEmail("paul@example.com");
        member.setPassword("pass456");
        entityManager.persistAndFlush(member);

        Trip trip1 = new Trip();
        trip1.setName("Sortie 1");
        trip1.setCategory(saved);
        trip1.setCreator(member);
        entityManager.persistAndFlush(trip1);

        Trip trip2 = new Trip();
        trip2.setName("Sortie 2");
        trip2.setCategory(saved);
        trip2.setCreator(member);
        entityManager.persistAndFlush(trip2);

        List<Trip> trips = categoryDAO.getTripsByCategory(saved.getId());
        assertEquals(2, trips.size());
    }

    @Test
    public void testCategoryExists() {
        Category saved = entityManager.persistAndFlush(testCategory);
        assertTrue(categoryDAO.categoryExists(saved.getId()));
        assertFalse(categoryDAO.categoryExists(999L));
    }

    @Test
    public void testCount() {
        entityManager.persistAndFlush(testCategory);
        long count = categoryDAO.count();
        assertTrue(count >= 1);
    }
}