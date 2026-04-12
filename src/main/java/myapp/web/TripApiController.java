package myapp.web;

import myapp.dao.TripDAO;
import myapp.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur REST démontrant l'utilisation des DAOs.
 * Endpoints pour les opérations CRUD sur les sorties.
 */
@RestController
@RequestMapping("/api/trips")
public class TripApiController {

    @Autowired
    private TripDAO tripDAO;

    /**
     * GET /api/trips - Obtenir toutes les sorties
     */
    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        List<Trip> trips = tripDAO.getAllTrips();
        return ResponseEntity.ok(trips);
    }

    /**
     * GET /api/trips/{id} - Obtenir une sortie par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripById(@PathVariable Long id) {
        Optional<Trip> trip = tripDAO.getTripById(id);
        return trip.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/trips - Créer une nouvelle sortie
     */
    @PostMapping
    public ResponseEntity<Trip> createTrip(@RequestBody Trip trip) {
        try {
            Trip created = tripDAO.createTrip(trip);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/trips/{id} - Modifier une sortie
     */
    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(
            @PathVariable Long id,
            @RequestBody Trip trip) {
        Optional<Trip> existing = tripDAO.getTripById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Trip updated = existing.get();
        if (trip.getName() != null) {
            updated.setName(trip.getName());
        }
        if (trip.getDescription() != null) {
            updated.setDescription(trip.getDescription());
        }

        Trip result = tripDAO.updateTrip(updated);
        return ResponseEntity.ok(result);
    }

    /**
     * DELETE /api/trips/{id} - Supprimer une sortie
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        try {
            tripDAO.deleteTrip(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/trips/count - Obtenir le nombre total de sorties
     */
    @GetMapping("/stats/count")
    public ResponseEntity<Long> countTrips() {
        long count = tripDAO.count();
        return ResponseEntity.ok(count);
    }
}

