package myapp.repo;

import myapp.model.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testSaveAndFindMember() {
        Member member = new Member();
        member.setLastName("Doe");
        member.setFirstName("John");
        member.setEmail("john.doe@example.com");
        member.setPassword("password");

        Member savedMember = memberRepository.save(member);

        assertThat(savedMember.getId()).isNotNull();
        assertThat(memberRepository.findById(savedMember.getId())).isPresent();
    }

}
