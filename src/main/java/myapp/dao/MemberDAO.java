package myapp.dao;

import myapp.model.Member;
import myapp.model.Trip;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour la gestion des membres.
 * Définit les opérations indépendantes du choix de persistance.
 */
public interface MemberDAO {

    /**
     * Obtient la liste de tous les membres
     * @return Liste des membres
     */
    List<Member> getAllMembers();

    /**
     * Obtient un membre par son identifiant
     * @param id Identifiant du membre
     * @return Membre trouvé
     */
    Optional<Member> getMemberById(Long id);

    /**
     * Obtient un membre avec toutes ses sorties créées
     * @param id Identifiant du membre
     * @return Membre avec ses sorties
     */
    Optional<Member> getMemberWithTrips(Long id);

    /**
     * Crée un nouveau membre
     * @param member Membre à créer
     * @return Membre créé
     */
    Member createMember(Member member);

    /**
     * Modifie un membre existant
     * @param member Membre à modifier
     * @return Membre modifié
     */
    Member updateMember(Member member);

    /**
     * Supprime un membre
     * @param id Identifiant du membre à supprimer
     */
    void deleteMember(Long id);

    /**
     * Obtient les sorties créées par un membre
     * @param memberId Identifiant du membre
     * @return Liste des sorties créées par ce membre
     */
    List<Trip> getTripsByMember(Long memberId);

    /**
     * Obtient un membre par son email
     * @param email Email du membre
     * @return Membre trouvé
     */
    Optional<Member> getMemberByEmail(String email);

    /**
     * Vérifie si un membre existe
     * @param id Identifiant du membre
     * @return true si le membre existe
     */
    boolean memberExists(Long id);

    /**
     * Obtient le nombre total de membres
     * @return Nombre de membres
     */
    long count();
}

