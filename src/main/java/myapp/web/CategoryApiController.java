package myapp.web;

import myapp.dao.CategoryDAO;
import myapp.dao.MemberDAO;
import myapp.dao.TripDAO;
import myapp.model.Category;
import myapp.model.Member;
import myapp.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur REST démontrant l'utilisation des DAOs.
 * Endpoints pour les opérations CRUD sur les catégories.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryApiController {

    @Autowired
    private CategoryDAO categoryDAO;

    /**
     * GET /api/categories - Obtenir toutes les catégories
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryDAO.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * GET /api/categories/{id} - Obtenir une catégorie par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryDAO.getCategoryById(id);
        return category.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/categories/{id}/with-trips - Obtenir une catégorie avec ses sorties
     */
    @GetMapping("/{id}/with-trips")
    public ResponseEntity<Category> getCategoryWithTrips(@PathVariable Long id) {
        Optional<Category> category = categoryDAO.getCategoryWithTrips(id);
        return category.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/categories - Créer une nouvelle catégorie
     */
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        try {
            Category created = categoryDAO.createCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/categories/{id} - Modifier une catégorie
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id,
            @RequestBody Category category) {
        Optional<Category> existing = categoryDAO.getCategoryById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Category updated = existing.get();
        if (category.getName() != null) {
            updated.setName(category.getName());
        }

        Category result = categoryDAO.updateCategory(updated);
        return ResponseEntity.ok(result);
    }

    /**
     * DELETE /api/categories/{id} - Supprimer une catégorie
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            categoryDAO.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/categories/{id}/trips - Obtenir les sorties d'une catégorie
     */
    @GetMapping("/{id}/trips")
    public ResponseEntity<List<Trip>> getTripsByCategory(@PathVariable Long id) {
        List<Trip> trips = categoryDAO.getTripsByCategory(id);
        return ResponseEntity.ok(trips);
    }

    /**
     * GET /api/categories/count - Obtenir le nombre total de catégories
     */
    @GetMapping("/stats/count")
    public ResponseEntity<Long> countCategories() {
        long count = categoryDAO.count();
        return ResponseEntity.ok(count);
    }
}

