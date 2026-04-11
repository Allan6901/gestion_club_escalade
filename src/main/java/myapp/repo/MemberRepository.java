package myapp.repo;

import myapp.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * Recherche un membre par email
     */
    Optional<Member> findByEmail(String email);

    /**
     * Recherche un membre avec ses sorties associées
     */
    @Query("SELECT DISTINCT m FROM Member m LEFT JOIN FETCH m.trips WHERE m.id = :id")
    Optional<Member> findByIdWithTrips(@Param("id") Long id);

}
