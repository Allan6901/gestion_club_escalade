package myapp.service;

import myapp.dao.CategoryDAO;
import myapp.model.Category;
import myapp.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des catégories.
 * Utilise le DAO pour accéder aux données.
 * Encapsule la logique métier de l'application.
 */
@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    /**
     * Récupère toutes les catégories
     */
    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryDAO.getCategoryById(id);
    }

    /**
     * Récupère une catégorie avec toutes ses sorties
     */
    public Optional<Category> getCategoryWithTrips(Long categoryId) {
        return categoryDAO.getCategoryWithTrips(categoryId);
    }

    /**
     * Crée une nouvelle catégorie
     */
    public Category createCategory(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la catégorie ne peut pas être vide");
        }

        Category category = new Category();
        category.setName(name);
        return categoryDAO.createCategory(category);
    }

    /**
     * Modifie une catégorie existante
     */
    public Category updateCategory(Long categoryId, String name) {
        Optional<Category> category = categoryDAO.getCategoryById(categoryId);
        if (category.isEmpty()) {
            throw new IllegalArgumentException("Catégorie non trouvée : " + categoryId);
        }

        Category updated = category.get();
        if (name != null && !name.trim().isEmpty()) {
            updated.setName(name);
        }

        return categoryDAO.updateCategory(updated);
    }

    /**
     * Supprime une catégorie
     */
    public void deleteCategory(Long categoryId) {
        categoryDAO.deleteCategory(categoryId);
    }

    /**
     * Obtient les sorties d'une catégorie
     */
    public List<Trip> getTripsByCategory(Long categoryId) {
        return categoryDAO.getTripsByCategory(categoryId);
    }

    /**
     * Obtient le nombre total de catégories
     */
    public long getTotalCategories() {
        return categoryDAO.count();
    }
}

