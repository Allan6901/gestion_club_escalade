package myapp.jpa.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import myapp.jpa.dao.JpaDao;
import myapp.jpa.model.Car;
import myapp.jpa.model.Person;
import myapp.jpa.model.Address;

import java.util.Date;
import java.util.List;

@SpringBootTest
@Transactional // Permet de rollback les changements après le test
public class CarRelationTest {

    @Autowired
    JpaDao dao;

    @Test
    public void testAddCarToExistingOwner() {
        Address addr = new Address("Rue de la Paix", "Paris", "75000");
        Person p = new Person("Jean", "Dupont", addr, new Date());
        dao.add(p);

        Car c = new Car("AA-123-BB", "Peugeot 208");

        c.setOwner(p);

        dao.add(c);

        Car foundCar = dao.find(Car.class, "AA-123-BB");
        assertNotNull(foundCar);
        assertNotNull(foundCar.getOwner());
        assertEquals("Jean", foundCar.getOwner().getFirstName());

        System.out.println("Voiture : " + foundCar.getModel() + " appartient à " + foundCar.getOwner().getFirstName());
    }

    @Test
    public void testCascadeAndNamedQuery() {
        Person p = new Person("Elon", "Musk", new Address("SpaceX Way", "LA", "90001"), new Date());
        p.addCar(new Car("TS-001-LA", "Model S"));
        p.addCar(new Car("TS-002-LA", "Cybertruck"));

        dao.add(p);

        List<Person> owners = dao.findPersonsByCarModel("Model S");
        assertFalse(owners.isEmpty());
        assertEquals("Elon", owners.get(0).getFirstName());

        Person existing = dao.findPerson(p.getId());
        existing.addCar(new Car("TS-003-LA", "Roadster"));
        dao.update(existing);

        assertEquals(3, dao.findPerson(p.getId()).getCars().size());
    }
}