package myapp.dao;

import myapp.model.Member;
import myapp.model.Trip;
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
@Import(MemberDAOImpl.class)
public class MemberDAOTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MemberDAO memberDAO;

    private Member testMember;

    @BeforeEach
    public void setUp() {
        testMember = new Member();
        testMember.setLastName("Dupont");
        testMember.setFirstName("Jean");
        testMember.setEmail("jean.dupont@example.com");
        testMember.setPassword("password123");
    }

    @Test
    public void testCreateMember() {
        Member created = memberDAO.createMember(testMember);
        assertNotNull(created.getId());
        assertEquals("Dupont", created.getLastName());
        assertEquals("Jean", created.getFirstName());
        assertEquals("jean.dupont@example.com", created.getEmail());
    }

    @Test
    public void testCreateMemberWithNullLastName() {
        testMember.setLastName(null);
        assertThrows(IllegalArgumentException.class, () -> memberDAO.createMember(testMember));
    }

    @Test
    public void testCreateMemberWithNullFirstName() {
        testMember.setFirstName(null);
        assertThrows(IllegalArgumentException.class, () -> memberDAO.createMember(testMember));
    }

    @Test
    public void testCreateMemberWithNullEmail() {
        testMember.setEmail(null);
        assertThrows(IllegalArgumentException.class, () -> memberDAO.createMember(testMember));
    }

    @Test
    public void testCreateMemberWithEmptyFields() {
        testMember.setLastName("   ");
        assertThrows(IllegalArgumentException.class, () -> memberDAO.createMember(testMember));
    }

    @Test
    public void testCreateMemberWithDuplicateEmail() {
        memberDAO.createMember(testMember);

        Member member2 = new Member();
        member2.setLastName("Martin");
        member2.setFirstName("Paul");
        member2.setEmail("jean.dupont@example.com");
        member2.setPassword("pass456");

        assertThrows(IllegalArgumentException.class, () -> memberDAO.createMember(member2));
    }

    @Test
    public void testCreateNullMember() {
        assertThrows(IllegalArgumentException.class, () -> memberDAO.createMember(null));
    }

    @Test
    public void testGetAllMembers() {
        Member member1 = new Member();
        member1.setLastName("Dupont");
        member1.setFirstName("Jean");
        member1.setEmail("jean@example.com");
        member1.setPassword("pass123");

        Member member2 = new Member();
        member2.setLastName("Martin");
        member2.setFirstName("Paul");
        member2.setEmail("paul@example.com");
        member2.setPassword("pass456");

        entityManager.persistAndFlush(member1);
        entityManager.persistAndFlush(member2);

        List<Member> members = memberDAO.getAllMembers();
        assertTrue(members.size() >= 2);
    }

    @Test
    public void testGetMemberById() {
        Member saved = entityManager.persistAndFlush(testMember);
        Optional<Member> found = memberDAO.getMemberById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Dupont", found.get().getLastName());
    }

    @Test
    public void testGetMemberByIdNotFound() {
        Optional<Member> found = memberDAO.getMemberById(999L);
        assertFalse(found.isPresent());
    }

    @Test
    public void testGetMemberByIdInvalid() {
        Optional<Member> found = memberDAO.getMemberById(null);
        assertFalse(found.isPresent());

        found = memberDAO.getMemberById(-1L);
        assertFalse(found.isPresent());
    }

    @Test
    public void testGetMemberByEmail() {
        entityManager.persistAndFlush(testMember);
        Optional<Member> found = memberDAO.getMemberByEmail("jean.dupont@example.com");

        assertTrue(found.isPresent());
        assertEquals("Jean", found.get().getFirstName());
    }

    @Test
    public void testGetMemberByEmailNotFound() {
        Optional<Member> found = memberDAO.getMemberByEmail("nonexistent@example.com");
        assertFalse(found.isPresent());
    }

    @Test
    public void testGetMemberWithTrips() {
        Member saved = entityManager.persistAndFlush(testMember);

        Optional<Member> found = memberDAO.getMemberWithTrips(saved.getId());
        assertTrue(found.isPresent());
    }

    @Test
    public void testUpdateMember() {
        Member saved = entityManager.persistAndFlush(testMember);
        saved.setLastName("Martin");

        Member updated = memberDAO.updateMember(saved);
        assertEquals("Martin", updated.getLastName());
    }

    @Test
    public void testUpdateNonExistentMember() {
        testMember.setId(999L);
        assertThrows(IllegalArgumentException.class, () -> memberDAO.updateMember(testMember));
    }

    @Test
    public void testDeleteMember() {
        Member saved = entityManager.persistAndFlush(testMember);
        memberDAO.deleteMember(saved.getId());

        assertFalse(memberDAO.memberExists(saved.getId()));
    }

    @Test
    public void testDeleteNonExistentMember() {
        assertThrows(IllegalArgumentException.class, () -> memberDAO.deleteMember(999L));
    }

    @Test
    public void testMemberExists() {
        Member saved = entityManager.persistAndFlush(testMember);
        assertTrue(memberDAO.memberExists(saved.getId()));
        assertFalse(memberDAO.memberExists(999L));
    }

    @Test
    public void testCount() {
        entityManager.persistAndFlush(testMember);
        long count = memberDAO.count();
        assertTrue(count >= 1);
    }
}