package myapp.jpa.dao;

import jakarta.transaction.Transactional;
import myapp.jpa.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TestJpaDao {

    @Autowired
    JpaDao dao;

    @Test
    public void addAndFindPerson() {
        // Création
        var p1 = new Person("Jean","Paule",new Address(), null);
        p1 = dao.addPerson(p1);
        assertTrue(p1.getId() > 0);
        // relecture
        var p2 = dao.findPerson(p1.getId());
        assertEquals("Jean", p2.getFirstName());
        assertEquals(p1.getId(), p2.getId());
    }

    @Test
    public void testNotNullException(){
        var p1 = new Person(null,null,null,null);
        assertThrows(DataIntegrityViolationException.class,()->{
            dao.addPerson(p1);
        },"Can't be null");
    }

    @Test
    public void testAddPersonWithCars(){
        var p1 = new Person("Jean","Paul",new Address(), null);
        p1.addCar(new Car("RB19", "Red Bull Racing"));
        p1.addCar(new Car("Valkyrie", "Aston Martin"));

        p1 = dao.addPerson(p1);
        assertTrue(p1.getId() > 0);

        Person p2 = dao.findPerson(p1.getId());

        assertNotNull(p2);
        assertNotNull(p2.getCars());
        assertEquals(2,p2.getCars().size(),"La personne devrait avoir 2 voitures");
    }

    @Test
    public void testFindPersonsByFirstName() {
        Person p1 = new Person("Jonathan", "Joestar", new Address(), new Date(68, 3, 4));
        Person p2 = new Person("Joseph", "Joestarr", new Address(), new Date(89, 8, 20));
        Person p3 = new Person("Dio", "Brando", new Address(), new Date(67, 1, 1));

        dao.addPerson(p1);
        dao.addPerson(p2);
        dao.addPerson(p3);

        List<Person> results = dao.findPersonsByFirstName("Jo%");

        assertNotNull(results, "La liste retournée ne doit pas être null");
        assertEquals(2, results.size(), "La requête doit trouver exactement 2 personnes (Jonathan et Joseph)");

        boolean foundJonathan = results.stream().anyMatch(p -> p.getFirstName().equals("Jonathan"));
        boolean foundJoseph = results.stream().anyMatch(p -> p.getFirstName().equals("Joseph"));
        boolean foundDio = results.stream().anyMatch(p -> p.getFirstName().equals("Dio"));

        assertTrue(foundJonathan, "Jonathan devrait être dans la liste");
        assertTrue(foundJoseph, "Joseph devrait être dans la liste");
        assertFalse(foundDio, "Dio ne correspond pas au motif et ne doit pas être dans la liste");
    }

    @Test
    public void testUpdatePersonAddCars(){
        var p1 = new Person("Jean","Pauls",new Address(), null);
        p1= dao.addPerson(p1);

        Person p2 = dao.findPerson(p1.getId());
        assertTrue(p2.getCars() == null || p2.getCars().isEmpty());

        p2.addCar(new Car("SF-23", "Ferrari"));
        dao.updatePerson(p2);
        Person p3 = dao.findPerson(p1.getId());
        assertNotNull(p3.getCars());
        assertEquals(1, p3.getCars().size(), "La personne devrait avoir 1 voiture après la mise à jour");
        assertEquals("Ferrari", p3.getCars().iterator().next().getModel());
    }

    @Test
    public void testUpdatePerson(){
        // Crée une personne sans birthday
        Person p1 = new Person("Bill", "Boul",new Address(),null);
        dao.addPerson(p1);

        p1.setBirthDay(new Date(2003,05,06));
        dao.updatePerson(p1);

        Person p2 = dao.findPerson(p1.getId());

        assertNotNull(p2.getBirthDay());
        assertEquals(new Date(2003,05,06), p2.getBirthDay());

        dao.removePerson(p1.getId());
    }

    @Test
    public void testRemovePerson(){
        var p1 = new Person("Bill","Boul" ,null,null);
        p1 = dao.addPerson(p1);
        assertEquals(p1.getId(),dao.findPerson(p1.getId()).getId());

        dao.removePerson(p1.getId());
        assertNull(dao.findPerson(p1.getId()));

    }

    @Test
    public void testUnityConstraint(){
        var p1 = new Person("Bro","tato" ,null,new Date(2003,05,06));
        p1 = dao.addPerson(p1);
        assertEquals(p1.getId(),dao.findPerson(p1.getId()).getId());

        var p2 = new Person("Bro","tito",null, new Date(2003,05,06));
        assertThrows(DataIntegrityViolationException.class,()->{
            dao.addPerson(p2);
        });
    }

    @Test
    public void testAddPersonThread() throws InterruptedException {

        var person = new Person("Wael", "Hilal",new Address(), new Date(2000, 1, 1));
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch startLatch = new CountDownLatch(1);
        Callable<Person> task = () -> {
            startLatch.await();
            return dao.addPerson(person);
        };

        Future<Person> futureA = executor.submit(task);
        Future<Person> futureB = executor.submit(task);

        startLatch.countDown();

        int succes = 0;
        int echecs = 0;


        try {
            futureA.get();
            succes++;
        } catch (ExecutionException e) {
            echecs++;
        }


        try {
            futureB.get();
            succes++;
        } catch (ExecutionException e) {
            echecs++;
        }


        executor.shutdown();

        assertEquals(1, succes, "Une des deux insertions doit réussir");
        assertEquals(1, echecs, "L'autre insertion doit échouer à cause du conflit de transaction/unicité");
    }


    @Test
    public void testFindAllFirstNames() {
        var p1 = new Person("Alice", "Wonder",new Address(), new Date(100, 1, 1));
        var p2 = new Person("Bob", "Marley",new Address(), new Date(101, 2, 2));
        dao.addPerson(p1);
        dao.addPerson(p2);

        List<FirstName> firstNames = dao.findAllFirstNames();

        assertNotNull(firstNames);
        assertTrue(firstNames.size() >= 2);

        boolean foundAlice = firstNames.stream()
                .anyMatch(f -> f.getFirstName().equals("Alice") && f.getId() == p1.getId());
        assertTrue(foundAlice);
    }

    @Test
    public void testEmbeddedAddress() {
        var address = new Address("10 rue de la Paix", "Paris", "France");
        var p1 = new Person("Gaston", "Lagaffe",new Address(), new Date(95, 2, 28));
        p1.setAddress(address);

        p1 = dao.addPerson(p1);

        var p2 = dao.findPerson(p1.getId());

        assertNotNull(p2.getAddress());
        assertEquals("Paris", p2.getAddress().getCity());
        assertEquals("France", p2.getAddress().getCountry());
        assertEquals("10 rue de la Paix", p2.getAddress().getStreet());
    }

    @Test
    public void testFindPersonsByCarModel() {
        Person p = new Person("Lewis", "Hamilton", null, new java.util.Date());
        p.addCar(new Car("F1-44-LH", "Mercedes-AMG"));
        dao.add(p); // Sauvegarde tout en cascade

        List<Person> owners = dao.findPersonsByCarModel("Mercedes-AMG");

        assertFalse(owners.isEmpty(), "On devrait trouver au moins un propriétaire");
        assertEquals("Lewis", owners.get(0).getFirstName());

        System.out.println("Propriétaire trouvé pour le modèle Mercedes-AMG : " + owners.get(0).getFirstName());
    }

    @Test
    @org.springframework.transaction.annotation.Transactional
    public void testAddAndRemoveMovie() {
        Person p1 = new Person("Quentinn", "Tarantino", new Address(), new Date(63, 2, 27));
        Movie m1 = new Movie("Pulp Fiction");

        p1.addMovie(m1);
        p1 = dao.addPerson(p1);


        long personId = p1.getId();

        Person p2 = dao.findPerson(personId);
        assertNotNull(p2.getMovies());
        assertEquals(1, p2.getMovies().size());

        Movie savedMovie = p2.getMovies().iterator().next();
        assertEquals("Pulp Fiction", savedMovie.getName());

        p2.getMovies().remove(savedMovie);
        dao.updatePerson(p2);

        Person p3 = dao.findPerson(personId);
        assertTrue(p3.getMovies().size() == 1);
    }

    @Test
    @org.springframework.transaction.annotation.Transactional
    public void testUEInheritance() {
        UE ue = new UE("UE101", 3);
        LicenceUE licenceUE = new LicenceUE("UE201", 6, "Algorithmique");
        MasterUE masterUE = new MasterUE("UE301", 9, "Intelligence Artificielle");

        dao.add(ue);
        dao.add(licenceUE);
        dao.add(masterUE);

        Collection<UE> allUEs = dao.findAll(UE.class);
        assertEquals(3, allUEs.size());

        Collection<LicenceUE> allLicenceUEs = dao.findAll(LicenceUE.class);
        assertEquals(1, allLicenceUEs.size());
        assertEquals("UE201", allLicenceUEs.iterator().next().getCode());

        Collection<MasterUE> allMasterUEs = dao.findAll(MasterUE.class);
        assertEquals(1, allMasterUEs.size());
        assertEquals("UE301", allMasterUEs.iterator().next().getCode());
    }

    @Test
    @Transactional
    public void testFindByFirstName() {
        Person p1 = new Person("Luke", "Skywalker", new Address(), new Date(77, 4, 25));
        Person p2 = new Person("Leia", "Organa", new Address(), new Date(78, 5, 26));

        dao.addPerson(p1);
        dao.addPerson(p2);

        List<Person> results = dao.findByFirstName("Luke");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Luke", results.get(0).getFirstName());
    }

    @Test
    @Transactional
    public void testFindByFirstNameLike() {
        Person p1 = new Person("Mario", "Bros", new Address(), new Date(81, 6, 9));
        Person p2 = new Person("Maria", "Robotnik", new Address(), new Date(91, 5, 23));
        Person p3 = new Person("Luigi", "Verdi", new Address(), new Date(83, 6, 14));

        dao.addPerson(p1);
        dao.addPerson(p2);
        dao.addPerson(p3);

        List<Person> results = dao.findByFirstNameLike("Mari%");

        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(p -> p.getFirstName().equals("Mario")));
        assertTrue(results.stream().anyMatch(p -> p.getFirstName().equals("Maria")));
        assertFalse(results.stream().anyMatch(p -> p.getFirstName().equals("Luigi")));
    }


}