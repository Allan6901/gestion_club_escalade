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
import java.util.ArrayList;
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
        logger.info("Démarrage de l'initialisation des données...");

        // === 30 catégories ===
        List<String> categoryNames = List.of(
            "Alpinisme de roche", "Alpinisme de neige", "Alpinisme mixte",
            "Escalade sportive", "Escalade en grande voie", "Escalade en falaise",
            "Bloc outdoor", "Bloc en salle",
            "Via ferrata débutant", "Via ferrata sportif",
            "Cascade de glace",
            "Ski de randonnée débutant", "Ski de randonnée confirmé", "Ski de randonnée expert",
            "Raquettes facile", "Raquettes sportif",
            "Trail running découverte", "Trail running semi-marathon", "Trail running ultra",
            "VTT cross-country", "VTT enduro", "VTT descente",
            "Randonnée pédestre", "Randonnée du vertige", "Randonnée glaciaire",
            "Terrain d'aventure", "Canyoning", "Escalade sur glace",
            "Bivouac montagne", "Multi-activités"
        );
        for (String name : categoryNames) {
            Category c = new Category();
            c.setName(name);
            categoryRepository.save(c);
        }
        logger.info("Catégories créées : {}", categoryRepository.count());

        // === 1 admin connu ===
        Member admin = new Member();
        admin.setLastName("Admin");
        admin.setFirstName("Super");
        admin.setEmail("admin@test.com");
        admin.setPassword("admin");
        admin.setRole("ADMIN");
        memberRepository.save(admin);

        // === 300 membres ===
        String[] firstNames = {
            "Alice", "Antoine", "Baptiste", "Camille", "Claire", "David", "Delphine", "Emma",
            "Étienne", "François", "Gabrielle", "Hugo", "Inès", "Julien", "Karine", "Laura",
            "Louis", "Lucas", "Lucie", "Marie", "Mathieu", "Mathilde", "Nicolas", "Noémie",
            "Olivia", "Pierre", "Quentin", "Romain", "Sarah", "Sophie",
            "Thomas", "Ugo", "Valentin", "Virginie", "William", "Xavier", "Yasmine", "Zoé",
            "Alexis", "Anaïs", "Arnaud", "Audrey", "Axel", "Bastien", "Benjamin", "Cécile",
            "Charles", "Charlotte", "Chloé", "Clément"
        };
        String[] lastNames = {
            "Martin", "Bernard", "Dubois", "Thomas", "Robert", "Richard", "Petit", "Durand",
            "Leroy", "Moreau", "Simon", "Laurent", "Lefebvre", "Michel", "Garcia", "David",
            "Bertrand", "Roux", "Vincent", "Fournier", "Morel", "Girard", "André", "Lefevre",
            "Mercier", "Dupont", "Lambert", "Bonnet", "François", "Martinez",
            "Leclerc", "Robin", "Muller", "Henry", "Rousseau", "Blanc", "Guerin", "Boyer",
            "Garnier", "Chevalier", "Faure", "Mathieu", "Colin", "Gauthier", "Clement",
            "Nguyen", "Perez", "Renard", "Barbier", "Bourgeois"
        };

        Random random = new Random(42);
        for (int i = 1; i <= 300; i++) {
            Member member = new Member();
            member.setFirstName(firstNames[i % firstNames.length]);
            member.setLastName(lastNames[i % lastNames.length] + i);
            member.setEmail("membre" + i + "@escalade.fr");
            member.setPassword("pass" + i);
            member.setRole("MEMBER");
            memberRepository.save(member);
        }
        logger.info("Membres créés : {}", memberRepository.count());

        // === 2000 sorties ===
        List<Category> categories = categoryRepository.findAll();
        List<Member> members = memberRepository.findAll();

        String[] activities = {
            "Escalade sportive", "Grande voie", "Bloc", "Via ferrata",
            "Alpinisme", "Cascade de glace", "Ski de randonnée", "Raquettes",
            "Trail running", "VTT enduro", "Randonnée glaciaire",
            "Bivouac", "Canyoning", "Course mixte", "Ski alpinisme"
        };
        String[] locations = {
            "Calanques", "Gorges du Verdon", "Fontainebleau", "Chamonix",
            "Écrins", "Vercors", "Chartreuse", "Belledonne", "Vanoise",
            "Mont-Blanc", "Aravis", "Bauges", "Pyrénées", "Mercantour",
            "Queyras", "Ubaye", "Dolomites", "Haute-Savoie", "Isère", "Var",
            "Ardèche", "Cévennes", "Corse", "Provence", "Dévoluy",
            "Oisans", "Belledonne nord", "Taillefer", "Champsaur", "Dévoluy"
        };
        String[] levels = {"débutant", "intermédiaire", "confirmé", "avancé"};
        String[] durations = {"1 jour", "2 jours", "week-end", "3 jours"};
        String[] descTemplates = {
            "%s dans le secteur des %s. Niveau %s recommandé. Départ à 8h00, retour vers 18h00. " +
                "Matériel fourni pour les participants, prévoir pique-nique et boissons.",
            "Journée de %s dans le massif des %s. Sortie réservée aux grimpeurs de niveau %s minimum. " +
                "Durée : %s. Co-voiturage organisé depuis Grenoble.",
            "Sortie %s en %s. Niveau %s, ouvert à tous avec expérience préalable. " +
                "Prévoir tenue adaptée, chaussures d'approche et protections solaires.",
            "Week-end %s au cœur des %s. Niveau %s. " +
                "Hébergement en refuge, repas du soir inclus. Inscription obligatoire avant J-7.",
            "Initiation et perfectionnement en %s sur le site des %s. " +
                "Idéal pour le niveau %s, matériel technique disponible au départ.",
            "Sortie club %s — secteur %s. Niveau %s, durée %s. " +
                "Départ groupé à 7h30, retour prévu en fin d'après-midi. Carpool possible.",
            "Programme %s dans le massif des %s. " +
                "Réservé aux membres de niveau %s ayant déjà participé à une sortie similaire. Durée %s.",
            "Découverte du %s aux %s. Niveau %s. " +
                "Encadrement par un guide breveté. Places limitées à 8 participants.",
        };

        List<Trip> tripsToSave = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            String activity   = activities[random.nextInt(activities.length)];
            String location   = locations[random.nextInt(locations.length)];
            String level      = levels[random.nextInt(levels.length)];
            String duration   = durations[random.nextInt(durations.length)];
            String descTpl    = descTemplates[random.nextInt(descTemplates.length)];
            // Certains templates ont 4 args, d'autres 2 ou 3 — on passe les 4 et laisse String.format ignorer les extras
            String description;
            try {
                description = String.format(descTpl, activity, location, level, duration);
            } catch (Exception e) {
                description = activity + " dans le secteur " + location + ". Niveau " + level + ".";
            }

            Trip trip = new Trip();
            trip.setName(activity + " — " + location + " (" + level + ")");
            trip.setDescription(description);
            trip.setWebsite("https://www.camptocamp.org/sorties/" + (i + 1));
            // Dates réparties sur les 2 prochaines années
            long offset = (long) random.nextInt(730) * 24 * 60 * 60 * 1000L;
            trip.setDate(new Date(System.currentTimeMillis() + offset));
            trip.setCreator(members.get(random.nextInt(members.size())));
            trip.setCategory(categories.get(random.nextInt(categories.size())));
            tripsToSave.add(trip);
        }
        tripRepository.saveAll(tripsToSave);

        logger.info("Données initialisées : {} catégories, {} membres, {} sorties.",
            categoryRepository.count(), memberRepository.count(), tripRepository.count());
    }
}