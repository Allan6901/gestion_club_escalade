package myapp.jpa;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import myapp.jpa.dao.PostRepository;
import myapp.jpa.dao.UserRepository;
import myapp.jpa.model.Comment;
import myapp.jpa.model.Post;
import myapp.jpa.model.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:hsqldb:file:target/testdb;shutdown=true",
        "spring.datasource.username=SA",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.hsqldb.jdbc.JDBCDriver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.hibernate.dialect=org.hibernate.dialect.HSQLDialect"
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(false)
public class EntityGraphTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private static Long samplePostId;

    @Test
    @Order(1)
    @Transactional
    public void populate() {
        for (int i = 1; i <= 5; i++) {
            User user = new User();
            user.setName("Utilisateur " + i);

            for (int j = 1; j <= 20; j++) {
                Post post = new Post();
                post.setTitle("Publication " + j + " par Utilisateur " + i);
                post.setUser(user);

                for (int k = 1; k <= 20; k++) {
                    Comment comment = new Comment();
                    comment.setText("Commentaire " + k + " sur Publication " + j);
                    comment.setPost(post);
                    post.getComments().add(comment);
                }
                user.getPosts().add(post);
            }
            userRepository.save(user);

            if (i == 1) {
                samplePostId = user.getPosts().get(0).getId();
            }
        }
    }

    @Test
    @Order(2)
    @Transactional(readOnly = true)
    public void testEntityGraphWithEntityManager() {
        EntityGraph<?> entityGraph = em.getEntityGraph("post-entity-graph");
        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.fetchgraph", entityGraph);

        Post post = em.find(Post.class, samplePostId, properties);

        assertNotNull(post);
        assertEquals(20, post.getComments().size());
        assertNotNull(post.getUser().getName());
    }

    @Test
    @Order(3)
    @Transactional(readOnly = true)
    public void testEntityGraphWithRepository() {
        Optional<Post> postOpt = postRepository.findById(samplePostId);

        assertTrue(postOpt.isPresent());
        Post post = postOpt.get();
        assertEquals(20, post.getComments().size());
        assertNotNull(post.getUser().getName());
    }

    @Test
    @Order(4)
    @Transactional(readOnly = true)
    public void testJoinFetchCommentsOnly() {
        Optional<Post> postOpt = postRepository.findByIdFetchCommentsOnly(samplePostId);

        assertTrue(postOpt.isPresent());
        Post post = postOpt.get();

        assertEquals(20, post.getComments().size());

        boolean userLoaded = em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(post, "user");
        assertFalse(userLoaded, "Le propriétaire de la publication ne doit pas être chargé dans ce fetch");
        
        assertNotNull(post.getUser().getName());
    }

    @Test
    @Order(5)
    @Transactional(readOnly = true)
    public void testPerformanceLazyVsEntityGraph() {
        em.clear();
        long lazyStart = System.nanoTime();
        Optional<Post> lazyPostOpt = postRepository.findById(samplePostId);
        assertTrue(lazyPostOpt.isPresent());
        Post lazyPost = lazyPostOpt.get();
        long lazyAfterFind = System.nanoTime();
        int lazyCommentsCount = lazyPost.getComments().size();
        long lazyAfterLoad = System.nanoTime();

        assertEquals(20, lazyCommentsCount);

        long lazyFetchTimeNanos = lazyAfterFind - lazyStart;
        long lazyLoadTimeNanos = lazyAfterLoad - lazyAfterFind;

        em.clear();
        long graphStart = System.nanoTime();
        Optional<Post> graphPostOpt = postRepository.findByIdFetchCommentsOnly(samplePostId);
        long graphAfterFind = System.nanoTime();

        assertTrue(graphPostOpt.isPresent());
        Post graphPost = graphPostOpt.get();
        long graphAfterLoad = System.nanoTime();

        assertEquals(20, graphPost.getComments().size());

        long graphFetchTimeNanos = graphAfterFind - graphStart;
        long graphTotalTimeNanos = graphAfterLoad - graphStart;

        System.out.println("lazy fetch: " + lazyFetchTimeNanos + " ns, lazy load comments: " + lazyLoadTimeNanos + " ns");
        System.out.println("graph fetch: " + graphFetchTimeNanos + " ns, graph total: " + graphTotalTimeNanos + " ns");

        assertEquals(20, graphPost.getComments().size());

        assertTrue(graphTotalTimeNanos <= (lazyFetchTimeNanos + lazyLoadTimeNanos) * 2,
                "La requête EntityGraph doit être comparable ou plus rapide que la résolution lazy en deux étapes");
    }
}