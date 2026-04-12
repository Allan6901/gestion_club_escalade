package myapp.web;

import myapp.dao.MemberDAO;
import myapp.model.Member;
import myapp.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur REST démontrant l'utilisation des DAOs.
 * Endpoints pour les opérations CRUD sur les membres.
 */
@RestController
@RequestMapping("/api/members")
public class MemberApiController {

    @Autowired
    private MemberDAO memberDAO;

    /**
     * GET /api/members - Obtenir tous les membres
     */
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberDAO.getAllMembers();
        return ResponseEntity.ok(members);
    }

    /**
     * GET /api/members/{id} - Obtenir un membre par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Optional<Member> member = memberDAO.getMemberById(id);
        return member.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/members/{id}/with-trips - Obtenir un membre avec ses sorties
     */
    @GetMapping("/{id}/with-trips")
    public ResponseEntity<Member> getMemberWithTrips(@PathVariable Long id) {
        Optional<Member> member = memberDAO.getMemberWithTrips(id);
        return member.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/members/email/{email} - Obtenir un membre par son email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Member> getMemberByEmail(@PathVariable String email) {
        Optional<Member> member = memberDAO.getMemberByEmail(email);
        return member.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/members - Créer un nouveau membre
     */
    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        try {
            Member created = memberDAO.createMember(member);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/members/{id} - Modifier un membre
     */
    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(
            @PathVariable Long id,
            @RequestBody Member member) {
        Optional<Member> existing = memberDAO.getMemberById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Member updated = existing.get();
        if (member.getLastName() != null) {
            updated.setLastName(member.getLastName());
        }
        if (member.getFirstName() != null) {
            updated.setFirstName(member.getFirstName());
        }
        if (member.getEmail() != null) {
            updated.setEmail(member.getEmail());
        }
        if (member.getPassword() != null) {
            updated.setPassword(member.getPassword());
        }

        Member result = memberDAO.updateMember(updated);
        return ResponseEntity.ok(result);
    }

    /**
     * DELETE /api/members/{id} - Supprimer un membre
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        try {
            memberDAO.deleteMember(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/members/{id}/trips - Obtenir les sorties créées par un membre
     */
    @GetMapping("/{id}/trips")
    public ResponseEntity<List<Trip>> getTripsByMember(@PathVariable Long id) {
        List<Trip> trips = memberDAO.getTripsByMember(id);
        return ResponseEntity.ok(trips);
    }

    /**
     * GET /api/members/stats/count - Obtenir le nombre total de membres
     */
    @GetMapping("/stats/count")
    public ResponseEntity<Long> countMembers() {
        long count = memberDAO.count();
        return ResponseEntity.ok(count);
    }
}

