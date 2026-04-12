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

        // Create a known admin for testing
        Member admin = new Member();
        admin.setLastName("Admin");
        admin.setFirstName("Super");
        admin.setEmail("admin@test.com");
        admin.setPassword("admin");
        admin.setRole("ADMIN");
        memberRepository.save(admin);

        // Create other random members
        List<Category> categories = categoryRepository.findAll();
        Random random = new Random();

        for (int i = 1; i <= 20; i++) {
            Member member = new Member();
            member.setLastName("LastName" + i);
            member.setFirstName("FirstName" + i);
            member.setEmail("member" + i + "@example.com");
            member.setPassword("password" + i);
            member.setRole("MEMBER");
            memberRepository.save(member);
        }
        logger.info("Members created: {}", memberRepository.count());

        // Create trips
        List<Member> members = memberRepository.findAll();

        String[][] tripData = {
            // {name, description, website}
            {"Grande voie aux Calanques - Dalle de la Candelle", "Magnifique grande voie de 5 longueurs en 6a/6b sur la dalle calcaire surplombant la mer. Équipement complet fourni, niveau débutant grande voie requis.", "https://www.camptocamp.org/routes/calanques"},
            {"Escalade en falaise - Gorges du Verdon", "Journée d'escalade dans les célèbres gorges du Verdon. Voies de 5c à 7a disponibles. Départ du Chalet de la Maline, retour par le sentier de l'Imbut.", "https://www.escalade-verdon.fr"},
            {"Bloc à Fontainebleau - Circuit Orange", "Session de bloc en forêt de Fontainebleau sur les rochers de Cuvier Rempart. Circuit orange (5b/6a). Transport groupé depuis Paris.", "https://www.bleauinfo.com"},
            {"Ascension du Mont Blanc par les Grands Mulets", "Ascension classique du toit de l'Europe (4808m) par la voie des Grands Mulets. 2 jours avec nuit au refuge. Niveau alpinisme confirmé.", "https://www.chamonix.com/mont-blanc"},
            {"Arête des Cosmiques - Aiguille du Midi", "Traversée de la célèbre arête des Cosmiques depuis l'Aiguille du Midi. Itinéraire technique à 3842m, crampons et piolet obligatoires.", "https://www.compagniedesguides.com/cosmiques"},
            {"Via Ferrata du Gorge de Daluis", "Via ferrata spectaculaire dans les gorges rouges du Var. Parcours famille (D) et parcours sportif (TD). Casque et kit via ferrata requis.", "https://www.daluis-via-ferrata.fr"},
            {"Cascade de glace à Fournel - L'Argentière-la-Bessée", "Week-end cascade de glace dans le vallon de Fournel. Voies de WI3 à WI5, briefing technique le soir, 2 jours sur glace.", "https://www.escalade-ecrins.fr/cascade-glace"},
            {"Escalade sportive à Sisteron - La Baume", "Journée sur les parois calcaires de La Baume à Sisteron. Secteur très ensoleillé, voies de 5a à 8a. Idéal pour perfectionnement technique.", "https://www.escalade-sisteron.com"},
            {"Ski de randonnée - Belledonne : Col de la Croix", "Randonnée à ski dans le massif de Belledonne jusqu'au Col de la Croix (2700m). Dénivelé +1200m, niveau intermédiaire en ski de randonnée.", "https://www.belledonne-randos.fr"},
            {"Raquettes en Chartreuse - Le Chamechaude", "Ascension en raquettes du Chamechaude (2082m), point culminant de la Chartreuse. Paysage grandiose, durée 5h, niveau intermédiaire.", "https://www.chartreuse-tourisme.com"},
            {"Grande voie au Vercors - Paroi des Écouges", "Belle grande voie de 7 longueurs (max 6b+) sur les parois calcaires des gorges des Écouges. Approche 45min, panorama exceptionnel.", "https://www.camptocamp.org/vercors"},
            {"Alpinisme - Traversée Meije", "Traversée de la Grande Meije (3983m), une des plus belles courses d'alpinisme des Écrins. 2 jours, niveau D, rocher et mixte.", "https://www.guide-meije.com"},
            {"Trail running - Tour du Massif des Bauges", "Trail de 42km autour du massif des Bauges, dénivelé +2800m. Ravitaillements tous les 10km. Niveau trail confirmé recommandé.", "https://www.traildesbauges.fr"},
            {"VTT enduro - Annecy le Semnoz", "Journée VTT enduro sur les pistes balisées du Semnoz. 4 descentes de difficulté croissante, navette assurée. Casque intégral conseillé.", "https://www.semnoz-vtt.com"},
            {"Escalade en falaise - Céüse", "Site d'escalade mondialement réputé au-dessus de Gap. Dénivelé d'approche 500m, voies de 6a à 9a. Organisation en deux groupes par niveau.", "https://www.escalade-ceuse.com"},
            {"Randonnée glaciaire - Glacier Blanc (Écrins)", "Traversée du Glacier Blanc avec ascension au Dôme des Écrins (4015m). Crampons, piolet et baudrier requis, encordement obligatoire.", "https://www.pre-de-madame-carle.com"},
            {"Escalade indoor - Stage perfectionnement technique", "Stage de 2 jours en salle pour travailler les techniques de mouvement, la pose de dégaines et la gestion de l'effort en voies longues.", "https://www.club-escalade.fr/stage"},
            {"Via Ferrata des Gorges de Trévans", "Via ferrata sportive dans les gorges de Trévans (Alpes de Haute-Provence). Ponts de singe, tyrolienne, passages aériens. Niveau AD.", "https://www.verdon-aventure.com"},
            {"Cascade de glace - Vallouise WI4", "Initiation et perfectionnement cascade de glace dans la vallée de Vallouise. Voies WI3 et WI4, matériel fourni pour les débutants.", "https://www.vallouise-escalade.fr"},
            {"Escalade Calanque de Morgiou - Les Toits", "Escalade en falaise dans la calanque de Morgiou, secteur des Toits. Voies polyvalentes de 5c à 7b+, prise en main des techniques de mouvement.", "https://www.escalade-calanques.fr"},
            {"Alpinisme initiation - Refuge du Goûter", "Week-end d'initiation alpinisme avec nuit au refuge du Goûter (3835m). Techniques de base sur neige et glace, préparation à une future ascension du Mont Blanc.", "https://www.refuge-gouter.fr"},
            {"Bloc - Circuit Bleu Fontainebleau (Éléphant)", "Découverte ou confirmation du bloc en forêt sur le célèbre rocher de l'Éléphant. Circuit bleu (4a/5a) et circuit rouge (5c/6b).", "https://www.bleauinfo.com/elephant"},
            {"Grande voie - Falaise des Gaillands (Chamonix)", "Initiation grande voie aux Gaillands de Chamonix. Escalade au pied du massif du Mont Blanc, voies de 4c à 6a, 3 à 6 longueurs.", "https://www.chamonix-escalade.com/gaillands"},
            {"Ski de randonnée - Belledonne : Croix de Chamrousse", "Montée à ski de la Croix de Chamrousse (2253m) depuis Revel. Dénivelé +1000m, très belle vue sur Grenoble et les Alpes.", "https://www.chamrousse.com/ski-randonnee"},
            {"Trail running - Oisans Ultra Trail (50km)", "Participation collective à l'OISANS UTMB (50km, +3200m). Préparation commune et soutien entre membres lors de la course.", "https://www.utmb.world/oisans"},
            {"Escalade sportive - Claret (Hérault)", "Journée escalade sur le calcaire massif de Claret près de Montpellier. Secteur varié, voies de 5b à 8b+, parfait pour les progrès.", "https://www.escalade-herault.fr/claret"},
            {"Randonnée glaciaire - Tour Ronde (Chamonix)", "Course classique à la Tour Ronde (3792m) depuis le refuge des Cosmiques. Itinéraire mixte rocher/glace, niveau D, 2 jours.", "https://www.monblancnaturalresort.com/tour-ronde"},
            {"Via Ferrata de Chamechaude - Voie des Bans", "Via ferrata aérienne sur les falaises de Chamechaude en Chartreuse. Longueur 800m, difficulté TD, magnifique panorama sur Grenoble.", "https://www.chartreuse-escalade.fr/chamechaude"},
            {"Ski de randonnée - Haute Route Chamonix-Zermatt (J1-J3)", "Premiers jours de la mythique Haute Route reliant Chamonix à Zermatt. 7 jours au total, nuits en refuge, magnifiques paysages alpins.", "https://www.haute-route.com"},
            {"Raquettes - Cirque de Gavarnie (Pyrénées)", "Randonnée en raquettes jusqu'au pied de la Grande Cascade de Gavarnie (423m), la plus haute cascade d'Europe. Paysage pyrénéen grandiose.", "https://www.gavarnie.com/raquettes"},
            {"Escalade en falaise - Buoux (Luberon)", "Journée sur les legendaires parois de Buoux, hauts lieu de l'escalade française. Voies de 5c à 8c, travail en tête et en moulinette.", "https://www.escalade-luberon.fr/buoux"},
            {"Alpinisme - Traversée des Arêtes de la Vanoise", "Traversée alpine spectaculaire dans le Parc National de la Vanoise. 3 jours, itinéraire crêtes et glaciers entre 2800 et 3600m.", "https://www.vanoise-parcnational.fr"},
            {"Cascade de glace - Ailefroide (Écrins)", "Semaine cascade de glace à Ailefroide. Programme progressif WI2 à WI5, tous niveaux, hébergement au gîte du village.", "https://www.ailefroide.fr/cascade-glace"},
            {"Bloc - Le Cul de Chien (Fontainebleau)", "Session bloc au Cul de Chien, classique de Fontainebleau. Circuits de toutes couleurs, travail des projets, superbe ambiance.", "https://www.bleauinfo.com/cul-de-chien"},
            {"Escalade sportive - Les Dentelles de Montmirail", "Journée d'escalade dans les Dentelles de Montmirail au-dessus des vignes du Ventoux. Rocher calcaire magnifique, voies de 4c à 7c.", "https://www.escalade-dentelles.fr"},
            {"Grande voie - Verdon : La Demande (600m)", "Grande voie mythique au Verdon, 600 mètres dans les gorges les plus profondes d'Europe. 2 jours, max 6b+, bivouac possible.", "https://www.verdon-escalade.fr/la-demande"},
            {"VTT cross-country - Chartreuse : Rando Sommet Vert", "Sortie VTT cross-country dans la Chartreuse, départ du Col de Porte. 40km, +1500m de dénivelé, paysages de forêts et alpages.", "https://www.chartreuse-vtt.fr"},
            {"Randonnée glaciaire - Les Grandes Jorasses", "Approche et vue des Grandes Jorasses depuis la Mer de Glace. Randonnée glaciaire encordée niveau débutant, initiation glacier.", "https://www.chamonix.com/grandes-jorasses"},
            {"Escalade en salle - Stage débutant complet", "Stage week-end pour grands débutants : assurage, nœuds, grimpe en moulinette et initiation en tête. Tout le matériel est fourni.", "https://www.club-escalade.fr/debutants"},
            {"Trail running - Belledonne : Le Grand Replomb", "Trail 30km autour du Grand Replomb en Belledonne. +2000m, panorama 360° depuis les crêtes, ravitaillement au sommet.", "https://www.belledonne-trail.fr"},
            {"Via Ferrata - Pont du Diable (Haute-Savoie)", "Via ferrata familiale et sportive au Pont du Diable dans les gorges de l'Arve. Parcours rouge (TD), ponts himalayens, tyrolienne finale.", "https://www.pont-du-diable.com"},
            {"Ski de randonnée - Les Aravis : La Croix de Fer", "Belle randonnée à ski aux Aravis vers la Croix de Fer (2345m). Départ des Confins, dénivelé +900m, neige souvent excellente en mars.", "https://www.aravis-ski-randonnee.fr"},
            {"Escalade - Stage grimpe en tête (perfectionnement)", "Week-end dédié à la grimpe en tête : gestion du souffle, placement des dégaines, chutes maîtrisées. En salle et en falaise suivant météo.", "https://www.club-escalade.fr/stage-tete"},
            {"Alpinisme - Les Écrins : Pic du Glacier d'Arsine", "Ascension du Pic du Glacier d'Arsine (3430m) depuis Villeneuve-la-Salle. Itinéraire mixte, longueur 5h aller-retour, niveau AD.", "https://www.ecrins-escalade.fr"},
            {"Grande voie - Rochers de Leschaux (Vercors)", "Grande voie sur les impressionnantes falaises des Rochers de Leschaux. Calcaire compact, voies de 4c à 6c, approche 30 min depuis la route.", "https://www.vercors-escalade.fr/leschaux"},
            {"Raquettes - Massif du Dévoluy : La Jarjatte", "Randonnée en raquettes dans le vallon isolé de la Jarjatte au cœur du Dévoluy. Paysage sauvage, faune alpine (chamois, lièvres variables).", "https://www.devoluy.fr/raquettes"},
            {"Cascade de glace - La Grave : Les Traversées", "Escalade de cascades de glace à La Grave au pied du glacier de la Meije. Voies variées WI3 à WI6, niveau intermédiaire à avancé.", "https://www.lagrave-lameije.com/cascade"},
            {"Bloc outdoor - Orgon (Bouches-du-Rhône)", "Sortie bloc sur les rochers calcaires d'Orgon dans le Luberon. Problèmes de niveau 4 à 8A, ambiance détendue et soleil garanti.", "https://www.bloc-provence.fr/orgon"},
            {"Escalade - Grande voie Ailefroide : Voie des Dalles", "Grande voie de 8 longueurs (max 6b) sur les granites et calcaires d'Ailefroide. Magnifique cadre écrins, approche 1h, réchappe facile.", "https://www.ailefroide.fr/grande-voie"},
            {"Trail running - Écotrail du Mercantour (42km)", "Trail 42km dans le Parc National du Mercantour. Paysages méditerranéens et alpins, +2500m de dénivelé, passage à 2800m d'altitude.", "https://www.mercantour-trail.fr"}
        };

        for (int i = 0; i < tripData.length; i++) {
            Trip trip = new Trip();
            trip.setName(tripData[i][0]);
            trip.setDescription(tripData[i][1]);
            trip.setWebsite(tripData[i][2]);
            trip.setDate(new Date(System.currentTimeMillis() + (long) random.nextInt(365) * 24 * 60 * 60 * 1000));
            trip.setCreator(members.get(random.nextInt(members.size())));
            trip.setCategory(categories.get(random.nextInt(categories.size())));
            tripRepository.save(trip);
        }

        logger.info("Data initialized: {} categories, {} members, {} trips.", categories.size(), members.size(), tripRepository.count());
    }
}
