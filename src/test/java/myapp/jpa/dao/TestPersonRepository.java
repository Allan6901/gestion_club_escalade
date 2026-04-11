package myapp.jpa.dao;

import java.util.Date;
import java.util.Optional;

import jakarta.transaction.Transactional;
import myapp.jpa.model.*;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TestPersonRepository {

    @Autowired
    PersonRepository dao;

    @Autowired
    UserRepository userRepository;

    @Test
    public void testRepository() {
        dao.deleteAll();
        assertFalse(dao.findAll().iterator().hasNext());
        var p = new Person("AAA","BBB",new Address(), new Date());
        dao.save(p);
        var op = dao.findById(p.getId());
        assertTrue(op.isPresent());
        p = op.get();
        assertEquals("AAA", p.getFirstName());
    }

    @Test
    @Order(4)
    @Transactional
    public void testUserWithPostsEntityGraph() {
        User u = new User();
        u.setName("Test User");
        Post p = new Post();
        p.setTitle("Test Post");
        p.setUser(u);
        u.getPosts().add(p);
        userRepository.save(u);

        Optional<User> userOpt = userRepository.findWithPostsById(u.getId());
        assertTrue(userOpt.isPresent());
        User user = userOpt.get();
        assertFalse(user.getPosts().isEmpty());
    }

    @Test
    @Order(5)
    @Transactional
    public void testUserWithCommentsEntityGraph() {
        User u = new User();
        u.setName("Test User 2");
        Comment c = new Comment();
        c.setText("Test Comment");
        c.setUser(u);
        u.getComments().add(c);
        userRepository.save(u);

        Optional<User> userOpt = userRepository.findWithCommentsById(u.getId());
        assertTrue(userOpt.isPresent());
        User user = userOpt.get();
        assertFalse(user.getComments().isEmpty());
    }

    @Test
    @Order(6)
    @Transactional
    public void testUserWithPostsAndCommentsLimitation() {
        User u = new User();
        u.setName("Test User 3");
        userRepository.save(u);

        assertThrows(Exception.class, () -> {
            userRepository.findWithPostsAndCommentsById(u.getId());
        });
    }

}