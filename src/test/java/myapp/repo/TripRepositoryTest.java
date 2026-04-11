package myapp.repo;

import myapp.model.Category;
import myapp.model.Member;
import myapp.model.Trip;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class TripRepositoryTest {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testSaveAndFindTrip() {
        Member member = new Member();
        member.setLastName("Doe");
        member.setFirstName("John");
        member.setEmail("john.doe@example.com");
        member.setPassword("password");
        member = memberRepository.save(member);

        Category category = new Category();
        category.setName("Alpinisme de roche");
        category = categoryRepository.save(category);

        Trip trip = new Trip();
        trip.setName("Sortie en montagne");
        trip.setDescription("Une belle sortie");
        trip.setWebsite("http://example.com");
        trip.setDate(Date.valueOf("2023-10-01"));
        trip.setCreator(member);
        trip.setCategory(category);

        Trip savedTrip = tripRepository.save(trip);

        assertThat(savedTrip.getId()).isNotNull();
        assertThat(tripRepository.findById(savedTrip.getId())).isPresent();
    }

}
