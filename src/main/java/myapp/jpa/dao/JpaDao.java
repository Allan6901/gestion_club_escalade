package myapp.jpa.dao;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import myapp.jpa.model.FirstName;
import myapp.jpa.model.MasterUE;
import org.springframework.stereotype.Service;

import myapp.jpa.model.Person;
import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
@Transactional
@Service
public class JpaDao {

    @PersistenceContext
    EntityManager em;

    public Person addPerson(Person p) {
        em.persist(p);
        return (p);
    }

    private EntityManagerFactory factory = null;

    @PostConstruct
    public void init() {
        factory = Persistence.createEntityManagerFactory("myBase");
    }

    @PreDestroy
    public void close() {
        if (factory != null) {
            factory.close();
        }
    }


    /**
     *
     * Met à jour une personne
     */
    public void updatePerson(Person p){
        em.merge(p);
    }

    /**
     *
     *  Retire une personne
     */
    public void removePerson(long id){
        Person p = em.find(Person.class, id);
        em.remove(p);
    }

    /*
     * Charger une personne
     */
    public Person findPerson(long id) {
        Person p = em.find(Person.class, id);
        if (p != null && p.getCars() != null) {
            p.getCars().size();
        }
        return p;
    }

    public List<Person> findAllPersons() {
        String query = "SELECT p FROM Person p";
        TypedQuery<Person> q = em.createQuery(query, Person.class);
        return q.getResultList();
    }

    public List<Person> findPersonsByFirstName(String pattern) {
        return em.createNamedQuery("Person.findPersonsByFirstName", Person.class)
                .setParameter("pattern", pattern)
                .getResultList();
    }

    public List<Person> findByFirstName(String firstName) {
        return em.createQuery("SELECT p FROM Person p WHERE p.firstName = :firstName", Person.class)
                .setParameter("firstName", firstName)
                .getResultList();
    }

    public List<Person> findByFirstNameLike(String pattern) {
        return em.createQuery("SELECT p FROM Person p WHERE p.firstName LIKE :pattern", Person.class)
                .setParameter("pattern", pattern)
                .getResultList();
    }

    public List<Person> findPersonsByCarModel(String model) {
        return em.createNamedQuery("Person.findPersonsByCarModel", Person.class)
                .setParameter("model", model)
                .getResultList();
    }

    public List<FirstName> findAllFirstNames() {
        return em.createQuery("SELECT new myapp.jpa.model.FirstName(p.id, p.firstName) FROM Person p", FirstName.class)
                .getResultList();
    }

    public <T> T find(Class<T> clazz, Object id) {
        return em.find(clazz, id);
    }

    public <T> Collection<T> findAll(Class<T> clazz) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> rootEntry = cq.from(clazz);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    }

    public <T> T add(T entity) {
        em.persist(entity);
        return entity;
    }

    public <T> T update(T entity) {
        return em.merge(entity);
    }

    public <T> void remove(Class<T> clazz, Object pk) {
        T entity = em.find(clazz, pk);
        if (entity != null) {
            em.remove(entity);
        }
    }

    public void changeFirstName(long idPerson, String firstName) {
        Person p = em.find(Person.class, idPerson, LockModeType.PESSIMISTIC_WRITE);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        p.setFirstName(firstName);
    }


    public Collection<MasterUE> findMasterUeA6Credits() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MasterUE> cq = cb.createQuery(MasterUE.class);
        Root<MasterUE> ue = cq.from(MasterUE.class);
        cq.select(ue).where(cb.equal(ue.get("credits"), 6));
        TypedQuery<MasterUE> q = em.createQuery(cq);
        return q.getResultList();
    }


}