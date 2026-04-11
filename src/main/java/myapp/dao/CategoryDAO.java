package myapp.dao;

import myapp.model.Category;
import myapp.model.Trip;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour la gestion des catégories.
 * Définit les opérations indépendantes du choix de persistance.
 */
public interface CategoryDAO {

    /**
     * Obtient la liste de toutes les catégories
     * @return Liste des catégories
     */
    List<Category> getAllCategories();

    /**
     * Obtient une catégorie par son identifiant
     * @param id Identifiant de la catégorie
     * @return Catégorie trouvée
     */
    Optional<Category> getCategoryById(Long id);

    /**
     * Obtient une catégorie avec toutes ses sorties associées
     * @param id Identifiant de la catégorie
     * @return Catégorie avec ses sorties
     */
    Optional<Category> getCategoryWithTrips(Long id);

    /**
     * Crée une nouvelle catégorie
     * @param category Catégorie à créer
     * @return Catégorie créée
     */
    Category createCategory(Category category);

    /**
     * Modifie une catégorie existante
     * @param category Catégorie à modifier
     * @return Catégorie modifiée
     */
    Category updateCategory(Category category);

    /**
     * Supprime une catégorie
     * @param id Identifiant de la catégorie à supprimer
     */
    void deleteCategory(Long id);

    /**
     * Obtient les sorties d'une catégorie
     * @param categoryId Identifiant de la catégorie
     * @return Liste des sorties de cette catégorie
     */
    List<Trip> getTripsByCategory(Long categoryId);

    /**
     * Vérifie si une catégorie existe
     * @param id Identifiant de la catégorie
     * @return true si la catégorie existe
     */
    boolean categoryExists(Long id);

    /**
     * Obtient le nombre total de catégories
     * @return Nombre de catégories
     */
    long count();
}

