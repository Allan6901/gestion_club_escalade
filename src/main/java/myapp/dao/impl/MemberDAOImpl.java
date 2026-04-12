package myapp.dao.impl;

import myapp.dao.MemberDAO;
import myapp.model.Member;
import myapp.model.Trip;
import myapp.repo.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation JPA du DAO pour les membres.
 * Utilise Spring Data JPA pour les opérations de persistance.
 */
@Service
@Transactional
public class MemberDAOImpl implements MemberDAO {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Optional<Member> getMemberById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return memberRepository.findById(id);
    }

    @Override
    public Optional<Member> getMemberWithTrips(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return memberRepository.findByIdWithTrips(id);
    }

    @Override
    public Member createMember(Member member) {
        if (member == null ||
            member.getLastName() == null || member.getLastName().trim().isEmpty() ||
            member.getFirstName() == null || member.getFirstName().trim().isEmpty() ||
            member.getEmail() == null || member.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Membre invalide : tous les champs obligatoires doivent être remplis");
        }

        // Vérifier que l'email est unique
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Un membre avec cet email existe déjà");
        }

        return memberRepository.save(member);
    }

    @Override
    public Member updateMember(Member member) {
        if (member == null || member.getId() == null) {
            throw new IllegalArgumentException("Membre invalide pour la modification");
        }
        if (!memberRepository.existsById(member.getId())) {
            throw new IllegalArgumentException("Membre non trouvé avec l'id : " + member.getId());
        }
        return memberRepository.save(member);
    }

    @Override
    public void deleteMember(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID invalide pour suppression");
        }
        if (!memberRepository.existsById(id)) {
            throw new IllegalArgumentException("Membre non trouvé avec l'id : " + id);
        }
        memberRepository.deleteById(id);
    }

    @Override
    public List<Trip> getTripsByMember(Long memberId) {
        if (memberId == null || memberId <= 0) {
            return List.of();
        }
        Optional<Member> member = memberRepository.findById(memberId);
        return member.map(Member::getTrips).orElse(List.of());
    }

    @Override
    public Optional<Member> getMemberByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }
        return memberRepository.findByEmail(email);
    }

    @Override
    public boolean memberExists(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return memberRepository.existsById(id);
    }

    @Override
    public long count() {
        return memberRepository.count();
    }
}

