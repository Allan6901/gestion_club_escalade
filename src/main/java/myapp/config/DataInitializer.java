package myapp.config;

import myapp.model.Category;
import myapp.model.Member;
import myapp.model.Trip;
import myapp.repo.CategoryRepository;
import myapp.repo.MemberRepository;
import myapp.repo.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TripRepository tripRepository;

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            initializeData();
        }
    }

    private void initializeData() {
        logger.info("Starting data initialization...");
        // Create categories
        List<String> categoryNames = List.of(
            "Alpinisme de roche", "Escalade sportive", "Escalade en falaise",
            "Via ferrata", "Randonnée glaciaire", "Cascade de glace",
            "Ski de randonnée", "Raquette", "Trail running", "VTT"
        );

        for (String name : categoryNames) {
            Category category = new Category();
            category.setName(name);
            categoryRepository.save(category);
        }
        logger.info("Categories created: {}", categoryRepository.count());

        // Create members
        List<Category> categories = categoryRepository.findAll();
        Random random = new Random();

        for (int i = 1; i <= 20; i++) {
            Member member = new Member();
            member.setLastName("LastName" + i);
            member.setFirstName("FirstName" + i);
            member.setEmail("member" + i + "@example.com");
            member.setPassword("password" + i);
            memberRepository.save(member);
        }
        logger.info("Members created: {}", memberRepository.count());

        // Create trips
        List<Member> members = memberRepository.findAll();

        for (int i = 1; i <= 50; i++) {
            Trip trip = new Trip();
            trip.setName("Trip " + i);
            trip.setDescription("Description for trip " + i);
            trip.setWebsite("https://example.com/trip" + i);
            trip.setDate(new Date(System.currentTimeMillis() + (long) random.nextInt(365) * 24 * 60 * 60 * 1000));
            trip.setCreator(members.get(random.nextInt(members.size())));
            trip.setCategory(categories.get(random.nextInt(categories.size())));
            tripRepository.save(trip);
        }

        logger.info("Data initialized: {} categories, {} members, {} trips.", categories.size(), members.size(), tripRepository.count());
    }
}
