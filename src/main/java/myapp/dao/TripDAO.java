package myapp.dao;

import myapp.model.Trip;
import java.util.List;
import java.util.Optional;
import java.sql.Date;

/**
 * Interface DAO pour la gestion des sorties.
 * Définit les opérations indépendantes du choix de persistance.
 */
public interface TripDAO {

    /**
     * Obtient la liste de toutes les sorties
     * @return Liste des sorties
     */
    List<Trip> getAllTrips();

    /**
     * Obtient une sortie par son identifiant
     * @param id Identifiant de la sortie
     * @return Sortie trouvée
     */
    Optional<Trip> getTripById(Long id);

    /**
     * Crée une nouvelle sortie
     * @param trip Sortie à créer
     * @return Sortie créée
     */
    Trip createTrip(Trip trip);

    /**
     * Modifie une sortie existante
     * @param trip Sortie à modifier
     * @return Sortie modifiée
     */
    Trip updateTrip(Trip trip);

    /**
     * Supprime une sortie
     * @param id Identifiant de la sortie à supprimer
     */
    void deleteTrip(Long id);

    /**
     * Cherche des sorties en fonction du nom
     * @param name Nom (ou partie du nom) à rechercher
     * @return Liste des sorties correspondantes
     */
    List<Trip> searchTripsByName(String name);

    /**
     * Cherche des sorties en fonction de la catégorie
     * @param categoryId Identifiant de la catégorie
     * @return Liste des sorties de cette catégorie
     */
    List<Trip> searchTripsByCategory(Long categoryId);

    /**
     * Cherche des sorties créées par un membre
     * @param memberId Identifiant du créateur
     * @return Liste des sorties créées par ce membre
     */
    List<Trip> searchTripsByCreator(Long memberId);

    /**
     * Cherche des sorties en fonction de la date
     * @param startDate Date de début (incluse)
     * @param endDate Date de fin (incluse)
     * @return Liste des sorties entre ces dates
     */
    List<Trip> searchTripsByDateRange(Date startDate, Date endDate);

    /**
     * Cherche des sorties selon plusieurs critères
     * @param name Nom (optionnel)
     * @param categoryId Catégorie (optionnel)
     * @param memberId Créateur (optionnel)
     * @return Liste des sorties correspondantes
     */
    List<Trip> searchTrips(String name, Long categoryId, Long memberId);

    /**
     * Vérifie si une sortie existe
     * @param id Identifiant de la sortie
     * @return true si la sortie existe
     */
    boolean tripExists(Long id);

    /**
     * Obtient le nombre total de sorties
     * @return Nombre de sorties
     */
    long count();
}

