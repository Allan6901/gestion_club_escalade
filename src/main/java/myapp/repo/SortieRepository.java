package myapp.repo;

import myapp.model.Sortie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SortieRepository extends JpaRepository<Sortie, Long> {

}
