package myapp.service;

import myapp.dao.MemberDAO;
import myapp.model.Member;
import myapp.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des membres.
 * Utilise le DAO pour accéder aux données.
 * Encapsule la logique métier de l'application.
 */
@Service
@Transactional
public class MemberService {

    @Autowired
    private MemberDAO memberDAO;

    /**
     * Récupère tous les membres
     */
    public List<Member> getAllMembers() {
        return memberDAO.getAllMembers();
    }

    /**
     * Récupère un membre particulier
     */
    public Optional<Member> getMemberById(Long memberId) {
        return memberDAO.getMemberById(memberId);
    }

    /**
     * Récupère un membre avec toutes ses sorties
     */
    public Optional<Member> getMemberWithTrips(Long memberId) {
        return memberDAO.getMemberWithTrips(memberId);
    }

    /**
     * Crée un nouveau membre
     */
    public Member createMember(String lastName, String firstName, String email, String password) {
        if (lastName == null || lastName.trim().isEmpty() ||
            firstName == null || firstName.trim().isEmpty() ||
            email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Les champs obligatoires doivent être remplis");
        }

        Member member = new Member();
        member.setLastName(lastName);
        member.setFirstName(firstName);
        member.setEmail(email);
        member.setPassword(password);

        return memberDAO.createMember(member);
    }

    /**
     * Modifie un membre existant
     */
    public Member updateMember(Long memberId, String lastName, String firstName, String email) {
        Optional<Member> member = memberDAO.getMemberById(memberId);
        if (member.isEmpty()) {
            throw new IllegalArgumentException("Membre non trouvé : " + memberId);
        }

        Member updated = member.get();
        if (lastName != null && !lastName.trim().isEmpty()) {
            updated.setLastName(lastName);
        }
        if (firstName != null && !firstName.trim().isEmpty()) {
            updated.setFirstName(firstName);
        }
        if (email != null && !email.trim().isEmpty()) {
            updated.setEmail(email);
        }

        return memberDAO.updateMember(updated);
    }

    /**
     * Supprime un membre
     */
    public void deleteMember(Long memberId) {
        memberDAO.deleteMember(memberId);
    }

    /**
     * Obtient les sorties créées par un membre
     */
    public List<Trip> getTripsByMember(Long memberId) {
        return memberDAO.getTripsByMember(memberId);
    }

    /**
     * Authentification simple (exemple)
     */
    public Optional<Member> authenticate(String email, String password) {
        Optional<Member> member = memberDAO.getMemberByEmail(email);
        if (member.isPresent() && member.get().getPassword().equals(password)) {
            return member;
        }
        return Optional.empty();
    }

    /**
     * Obtient le nombre total de membres
     */
    public long getTotalMembers() {
        return memberDAO.count();
    }

    public Optional<Member> getMemberByEmail(String email) {
        return memberDAO.getMemberByEmail(email);
    }

    public Member createMember(Member member) {
        return memberDAO.createMember(member);
    }

    public Member updateMember(Member member) {
        return memberDAO.updateMember(member);
    }
}

