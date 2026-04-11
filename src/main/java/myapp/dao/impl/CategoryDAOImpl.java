package myapp.dao.impl;

import myapp.dao.CategoryDAO;
import myapp.model.Category;
import myapp.model.Trip;
import myapp.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation JPA du DAO pour les catégories.
 * Utilise Spring Data JPA pour les opérations de persistance.
 */
@Service
@Transactional
public class CategoryDAOImpl implements CategoryDAO {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<Category> getCategoryWithTrips(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return categoryRepository.findByIdWithTrips(id);
    }

    @Override
    public Category createCategory(Category category) {
        if (category == null || category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Catégorie invalide : le nom ne peut pas être vide");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        if (category == null || category.getId() == null) {
            throw new IllegalArgumentException("Catégorie invalide pour la modification");
        }
        if (!categoryRepository.existsById(category.getId())) {
            throw new IllegalArgumentException("Catégorie non trouvée avec l'id : " + category.getId());
        }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID invalide pour suppression");
        }
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Catégorie non trouvée avec l'id : " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Trip> getTripsByCategory(Long categoryId) {
        if (categoryId == null || categoryId <= 0) {
            return List.of();
        }
        return categoryRepository.findTripsByCategory(categoryId);
    }

    @Override
    public boolean categoryExists(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return categoryRepository.existsById(id);
    }

    @Override
    public long count() {
        return categoryRepository.count();
    }
}

