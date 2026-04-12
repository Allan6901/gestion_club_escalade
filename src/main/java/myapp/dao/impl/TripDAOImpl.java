package myapp.dao.impl;

import myapp.dao.TripDAO;
import myapp.model.Member;
import myapp.model.Trip;
import myapp.repo.MemberRepository;
import myapp.repo.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation JPA du DAO pour les sorties.
 * Utilise Spring Data JPA pour les opérations de persistance.
 */
@Service
@Transactional
public class TripDAOImpl implements TripDAO {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    @Override
    public Optional<Trip> getTripById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return tripRepository.findById(id);
    }

    @Override
    public Trip createTrip(Trip trip) {
        if (trip == null || 
            trip.getName() == null || trip.getName().trim().isEmpty() ||
            trip.getCreator() == null ||
            trip.getCategory() == null) {
            throw new IllegalArgumentException("Sortie invalide : le nom, le créateur et la catégorie sont obligatoires");
        }
        return tripRepository.save(trip);
    }

    @Override
    public Trip updateTrip(Trip trip) {
        if (trip == null || trip.getId() == null) {
            throw new IllegalArgumentException("Sortie invalide pour la modification");
        }
        if (!tripRepository.existsById(trip.getId())) {
            throw new IllegalArgumentException("Sortie non trouvée avec l'id : " + trip.getId());
        }
        return tripRepository.save(trip);
    }

    @Override
    public void deleteTrip(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID invalide pour suppression");
        }
        if (!tripRepository.existsById(id)) {
            throw new IllegalArgumentException("Sortie non trouvée avec l'id : " + id);
        }
        tripRepository.deleteById(id);
    }

    @Override
    public List<Trip> searchTripsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return List.of();
        }
        return tripRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Trip> searchTripsByCategory(Long categoryId) {
        if (categoryId == null || categoryId <= 0) {
            return List.of();
        }
        return tripRepository.findByCategory(categoryId);
    }

    @Override
    public List<Trip> searchTripsByCreator(Long memberId) {
        if (memberId == null || memberId <= 0) {
            return List.of();
        }
        return tripRepository.findByCreator(memberId);
    }

    @Override
    public List<Trip> searchTripsByDateRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return List.of();
        }
        return tripRepository.findByDateRange(startDate, endDate);
    }

    @Override
    public List<Trip> searchTrips(String name, Long categoryId, Long memberId) {
        return tripRepository.searchTrips(
            (name != null && !name.trim().isEmpty()) ? name : null,
            (categoryId != null && categoryId > 0) ? categoryId : null,
            (memberId != null && memberId > 0) ? memberId : null
        );
    }

    @Override
    public boolean tripExists(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return tripRepository.existsById(id);
    }

    @Override
    public long count() {
        return tripRepository.count();
    }

    @Override
    public void addParticipant(Long tripId, Long memberId) {
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new IllegalArgumentException("Sortie non trouvée : " + tripId));
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Membre non trouvé : " + memberId));
        if (trip.getParticipants().stream().noneMatch(m -> m.getId().equals(memberId))) {
            trip.getParticipants().add(member);
            tripRepository.save(trip);
        }
    }

    @Override
    public void removeParticipant(Long tripId, Long memberId) {
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new IllegalArgumentException("Sortie non trouvée : " + tripId));
        trip.getParticipants().removeIf(m -> m.getId().equals(memberId));
        tripRepository.save(trip);
    }

    @Override
    public boolean isParticipant(Long tripId, Long memberId) {
        return tripRepository.findById(tripId)
            .map(trip -> trip.getParticipants().stream().anyMatch(m -> m.getId().equals(memberId)))
            .orElse(false);
    }

    @Override
    public Page<Trip> getTripsByCategoryPageable(Long categoryId, Pageable pageable) {
        return tripRepository.findByCategoryPageable(categoryId, pageable);
    }

    @Override
    public Page<Trip> searchTripsPageable(String name, Long categoryId, Long memberId, Pageable pageable) {
        return tripRepository.searchTripsPageable(
            (name != null && !name.trim().isEmpty()) ? name : null,
            (categoryId != null && categoryId > 0) ? categoryId : null,
            (memberId != null && memberId > 0) ? memberId : null,
            pageable
        );
    }

    @Override
    public Page<Trip> getTripsByCreatorPageable(Long memberId, Pageable pageable) {
        return tripRepository.findByCreatorPageable(memberId, pageable);
    }
}

