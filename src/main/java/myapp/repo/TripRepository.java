package myapp.repo;

import myapp.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    /**
     * Recherche les sorties par nom (contient)
     */
    @Query("SELECT t FROM Trip t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Trip> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Recherche les sorties par catégorie
     */
    @Query("SELECT t FROM Trip t WHERE t.category.id = :categoryId")
    List<Trip> findByCategory(@Param("categoryId") Long categoryId);

    /**
     * Recherche les sorties créées par un membre
     */
    @Query("SELECT t FROM Trip t WHERE t.creator.id = :memberId")
    List<Trip> findByCreator(@Param("memberId") Long memberId);

    /**
     * Recherche les sorties dans une plage de dates
     */
    @Query("SELECT t FROM Trip t WHERE t.date BETWEEN :startDate AND :endDate ORDER BY t.date ASC")
    List<Trip> findByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * Recherche les sorties selon plusieurs critères
     */
    @Query("SELECT t FROM Trip t WHERE " +
           "(:name IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:categoryId IS NULL OR t.category.id = :categoryId) AND " +
           "(:memberId IS NULL OR t.creator.id = :memberId)")
    List<Trip> searchTrips(@Param("name") String name, 
                          @Param("categoryId") Long categoryId,
                          @Param("memberId") Long memberId);

}
