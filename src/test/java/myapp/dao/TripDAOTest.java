package myapp.dao;

import myapp.model.Category;
import myapp.model.Member;
import myapp.model.Trip;
import myapp.repo.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import myapp.dao.impl.TripDAOImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests JUnit pour le DAO TripDAO.
 * Valide les opérations CRUD et les requêtes de recherche.
 */
@DataJpaTest
@Import(TripDAOImpl.class)
public class TripDAOTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TripDAO tripDAO;

    private Trip testTrip;
    private Member testMember;
    private Category testCategory;

    @BeforeEach
    public void setUp() {
        // Créer un membre
        testMember = new Member();
        testMember.setLastName("Dupont");
        testMember.setFirstName("Jean");
        testMember.setEmail("jean@example.com");
        testMember.setPassword("pass123");
        testMember = entityManager.persistAndFlush(testMember);

        // Créer une catégorie
        testCategory = new Category();
        testCategory.setName("Escalade Sportive");
        testCategory = entityManager.persistAndFlush(testCategory);

        // Créer une sortie
        testTrip = new Trip();
        testTrip.setName("Sortie Test");
        testTrip.setDescription("Description test");
        testTrip.setWebsite("http://example.com");
        testTrip.setDate(Date.valueOf(LocalDate.now()));
        testTrip.setCreator(testMember);
        testTrip.setCategory(testCategory);
    }

    // ========== Tests CREATE ==========

    @Test
    public void testCreateTrip() {
        Trip created = tripDAO.createTrip(testTrip);
        assertNotNull(created.getId());
        assertEquals("Sortie Test", created.getName());
        assertEquals(testMember.getId(), created.getCreator().getId());
        assertEquals(testCategory.getId(), created.getCategory().getId());
    }

    @Test
    public void testCreateTripWithNullName() {
        testTrip.setName(null);
        assertThrows(IllegalArgumentException.class, () -> tripDAO.createTrip(testTrip));
    }

    @Test
    public void testCreateTripWithNullCreator() {
        testTrip.setCreator(null);
        assertThrows(IllegalArgumentException.class, () -> tripDAO.createTrip(testTrip));
    }

    @Test
    public void testCreateTripWithNullCategory() {
        testTrip.setCategory(null);
        assertThrows(IllegalArgumentException.class, () -> tripDAO.createTrip(testTrip));
    }

    @Test
    public void testCreateNullTrip() {
        assertThrows(IllegalArgumentException.class, () -> tripDAO.createTrip(null));
    }

    // ========== Tests READ ==========

    @Test
    public void testGetAllTrips() {
        entityManager.persistAndFlush(testTrip);

        Trip trip2 = new Trip();
        trip2.setName("Sortie 2");
        trip2.setCreator(testMember);
        trip2.setCategory(testCategory);
        trip2.setDate(Date.valueOf(LocalDate.now()));
        entityManager.persistAndFlush(trip2);

        List<Trip> trips = tripDAO.getAllTrips();
        assertTrue(trips.size() >= 2);
    }

    @Test
    public void testGetTripById() {
        Trip saved = entityManager.persistAndFlush(testTrip);
        Optional<Trip> found = tripDAO.getTripById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Sortie Test", found.get().getName());
    }

    @Test
    public void testGetTripByIdNotFound() {
        Optional<Trip> found = tripDAO.getTripById(999L);
        assertFalse(found.isPresent());
    }

    @Test
    public void testGetTripByIdInvalid() {
        Optional<Trip> found = tripDAO.getTripById(null);
        assertFalse(found.isPresent());

        found = tripDAO.getTripById(-1L);
        assertFalse(found.isPresent());
    }

    // ========== Tests UPDATE ==========

    @Test
    public void testUpdateTrip() {
        Trip saved = entityManager.persistAndFlush(testTrip);
        saved.setName("Sortie Modifiée");
        
        Trip updated = tripDAO.updateTrip(saved);
        assertEquals("Sortie Modifiée", updated.getName());
    }

    @Test
    public void testUpdateNonExistentTrip() {
        testTrip.setId(999L);
        assertThrows(IllegalArgumentException.class, () -> tripDAO.updateTrip(testTrip));
    }

    // ========== Tests DELETE ==========

    @Test
    public void testDeleteTrip() {
        Trip saved = entityManager.persistAndFlush(testTrip);
        tripDAO.deleteTrip(saved.getId());

        assertFalse(tripDAO.tripExists(saved.getId()));
    }

    @Test
    public void testDeleteNonExistentTrip() {
        assertThrows(IllegalArgumentException.class, () -> tripDAO.deleteTrip(999L));
    }

    // ========== Tests SEARCH ==========

    @Test
    public void testSearchTripsByName() {
        testTrip.setName("Escalade à Fontainebleau");
        entityManager.persistAndFlush(testTrip);

        List<Trip> results = tripDAO.searchTripsByName("Fontainebleau");
        assertFalse(results.isEmpty());
        assertTrue(results.get(0).getName().contains("Fontainebleau"));
    }

    @Test
    public void testSearchTripsByNameNotFound() {
        entityManager.persistAndFlush(testTrip);
        List<Trip> results = tripDAO.searchTripsByName("NonExistant");
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchTripsByCategory() {
        entityManager.persistAndFlush(testTrip);

        List<Trip> results = tripDAO.searchTripsByCategory(testCategory.getId());
        assertFalse(results.isEmpty());
        assertTrue(results.get(0).getCategory().getId().equals(testCategory.getId()));
    }

    @Test
    public void testSearchTripsByCreator() {
        entityManager.persistAndFlush(testTrip);

        List<Trip> results = tripDAO.searchTripsByCreator(testMember.getId());
        assertFalse(results.isEmpty());
        assertTrue(results.get(0).getCreator().getId().equals(testMember.getId()));
    }

    @Test
    public void testSearchTripsByDateRange() {
        testTrip.setDate(Date.valueOf(LocalDate.now()));
        entityManager.persistAndFlush(testTrip);

        Date startDate = Date.valueOf(LocalDate.now().minusDays(1));
        Date endDate = Date.valueOf(LocalDate.now().plusDays(1));

        List<Trip> results = tripDAO.searchTripsByDateRange(startDate, endDate);
        assertFalse(results.isEmpty());
    }

    @Test
    public void testSearchTripsWithMultipleCriteria() {
        entityManager.persistAndFlush(testTrip);

        List<Trip> results = tripDAO.searchTrips("Test", testCategory.getId(), testMember.getId());
        assertFalse(results.isEmpty());
    }

    @Test
    public void testSearchTripsWithOnlyName() {
        entityManager.persistAndFlush(testTrip);
        List<Trip> results = tripDAO.searchTrips("Test", null, null);
        assertFalse(results.isEmpty());
    }

    // ========== Tests UTILITIES ==========

    @Test
    public void testTripExists() {
        Trip saved = entityManager.persistAndFlush(testTrip);
        assertTrue(tripDAO.tripExists(saved.getId()));
        assertFalse(tripDAO.tripExists(999L));
    }

    @Test
    public void testCount() {
        entityManager.persistAndFlush(testTrip);
        long count = tripDAO.count();
        assertTrue(count >= 1);
    }
}

