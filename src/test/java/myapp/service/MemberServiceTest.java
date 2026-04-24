package myapp.service;

import myapp.dao.MemberDAO;
import myapp.model.Member;
import myapp.repo.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import myapp.dao.impl.MemberDAOImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({MemberDAOImpl.class, MemberService.class})
public class MemberServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MemberService memberService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testCreateMember() {
        Member created = memberService.createMember("Dupont", "Jean", "jean@example.com", "pass123");
        assertNotNull(created.getId());
        assertEquals("Dupont", created.getLastName());
    }

    @Test
    public void testCreateMemberWithEmptyLastName() {
        assertThrows(IllegalArgumentException.class,
            () -> memberService.createMember("", "Jean", "jean@example.com", "pass123"));
    }

    @Test
    public void testGetAllMembers() {
        memberService.createMember("Dupont", "Jean", "jean@example.com", "pass123");
        memberService.createMember("Martin", "Paul", "paul@example.com", "pass456");

        List<Member> members = memberService.getAllMembers();
        assertTrue(members.size() >= 2);
    }

    @Test
    public void testGetMemberById() {
        Member created = memberService.createMember("Dupont", "Jean", "jean@example.com", "pass123");
        Optional<Member> found = memberService.getMemberById(created.getId());

        assertTrue(found.isPresent());
        assertEquals("Jean", found.get().getFirstName());
    }

    @Test
    public void testAuthenticate() {
        memberService.createMember("Dupont", "Jean", "jean@example.com", "password123");

        Optional<Member> authenticated = memberService.authenticate("jean@example.com", "password123");
        assertTrue(authenticated.isPresent());

        Optional<Member> notAuthenticated = memberService.authenticate("jean@example.com", "wrongpass");
        assertFalse(notAuthenticated.isPresent());
    }

    @Test
    public void testGetTotalMembers() {
        memberService.createMember("Dupont", "Jean", "jean@example.com", "pass123");
        long count = memberService.getTotalMembers();
        assertTrue(count >= 1);
    }
}

