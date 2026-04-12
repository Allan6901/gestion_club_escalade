package myapp.repo;

import myapp.model.Category;
import myapp.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Recherche une catégorie avec ses sorties associées
     */
    @Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.trips WHERE c.id = :id")
    Optional<Category> findByIdWithTrips(@Param("id") Long id);

    /**
     * Obtient toutes les sorties d'une catégorie
     */
    @Query("SELECT t FROM Trip t WHERE t.category.id = :categoryId")
    List<Trip> findTripsByCategory(@Param("categoryId") Long categoryId);

}
