package myapp.jpa.dao;

import myapp.jpa.model.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(value = "post-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Post> findById(Long id);

    @Query("select p from Post p join fetch p.comments where p.id = :id")
    Optional<Post> findByIdFetchCommentsOnly(@Param("id") Long id);
}