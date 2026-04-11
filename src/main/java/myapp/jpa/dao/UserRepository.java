package myapp.jpa.dao;

import myapp.jpa.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(value = "user-with-posts", type = EntityGraph.EntityGraphType.FETCH)
    Optional<User> findWithPostsById(Long id);

    @EntityGraph(value = "user-with-comments", type = EntityGraph.EntityGraphType.FETCH)
    Optional<User> findWithCommentsById(Long id);

    @EntityGraph(value = "user-with-posts-and-comments", type = EntityGraph.EntityGraphType.FETCH)
    Optional<User> findWithPostsAndCommentsById(Long id);
}