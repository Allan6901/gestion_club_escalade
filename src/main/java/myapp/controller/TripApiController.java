package myapp.controller;

import myapp.model.Trip;
import myapp.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trips")
public class TripApiController {

    @Autowired
    private TripService tripService;

    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        List<Trip> trips = tripService.getAllTrips();
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripById(@PathVariable Long id) {
        Optional<Trip> trip = tripService.getTripDetails(id);
        return trip.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Trip> createTrip(@RequestBody Trip trip) {
        try {
            Trip created = tripService.createTrip(trip);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(
            @PathVariable Long id,
            @RequestBody Trip trip) {
        Optional<Trip> existing = tripService.getTripDetails(id);
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

        Trip result = tripService.updateTrip(updated);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        try {
            tripService.deleteTrip(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/stats/count")
    public ResponseEntity<Long> countTrips() {
        long count = tripService.getTotalTrips();
        return ResponseEntity.ok(count);
    }
}