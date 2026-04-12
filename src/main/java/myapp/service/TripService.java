package myapp.service;

import myapp.dao.CategoryDAO;
import myapp.dao.MemberDAO;
import myapp.dao.TripDAO;
import myapp.model.Category;
import myapp.model.Member;
import myapp.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des sorties (Trip).
 * Utilise les DAOs pour accéder aux données.
 * Encapsule la logique métier de l'application.
 */
@Service
@Transactional
public class TripService {

    @Autowired
    private TripDAO tripDAO;

    @Autowired
    private MemberDAO memberDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    /**
     * Crée une nouvelle sortie avec validations métier
     */
    public Trip createTrip(String name, String description, Long memberId, Long categoryId) {
        // Vérifier que le membre existe
        Optional<Member> member = memberDAO.getMemberById(memberId);
        if (member.isEmpty()) {
            throw new IllegalArgumentException("Membre non trouvé : " + memberId);
        }

        // Vérifier que la catégorie existe
        Optional<Category> category = categoryDAO.getCategoryById(categoryId);
        if (category.isEmpty()) {
            throw new IllegalArgumentException("Catégorie non trouvée : " + categoryId);
        }

        // Créer la sortie
        Trip trip = new Trip();
        trip.setName(name);
        trip.setDescription(description);
        trip.setCreator(member.get());
        trip.setCategory(category.get());

        return tripDAO.createTrip(trip);
    }

    /**
     * Récupère toutes les sorties d'une catégorie
     */
    public List<Trip> getTripsByCategory(Long categoryId) {
        return tripDAO.searchTripsByCategory(categoryId);
    }

    /**
     * Récupère toutes les sorties créées par un membre
     */
    public List<Trip> getTripsByMember(Long memberId) {
        return tripDAO.searchTripsByCreator(memberId);
    }

    /**
     * Recherche des sorties par critères
     */
    public List<Trip> searchTrips(String name, Long categoryId, Long memberId) {
        return tripDAO.searchTrips(name, categoryId, memberId);
    }

    /**
     * Obtient une sortie avec tous ses détails
     */
    public Optional<Trip> getTripDetails(Long tripId) {
        return tripDAO.getTripById(tripId);
    }

    /**
     * Modifie une sortie existante
     */
    public Trip updateTrip(Long tripId, String name, String description) {
        Optional<Trip> trip = tripDAO.getTripById(tripId);
        if (trip.isEmpty()) {
            throw new IllegalArgumentException("Sortie non trouvée : " + tripId);
        }

        Trip updated = trip.get();
        if (name != null && !name.trim().isEmpty()) {
            updated.setName(name);
        }
        if (description != null) {
            updated.setDescription(description);
        }

        return tripDAO.updateTrip(updated);
    }

    /**
     * Supprime une sortie
     */
    public void deleteTrip(Long tripId) {
        tripDAO.deleteTrip(tripId);
    }

    /**
     * Obtient le nombre total de sorties
     */
    public long getTotalTrips() {
        return tripDAO.count();
    }
}

