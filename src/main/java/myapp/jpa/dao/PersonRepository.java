package myapp.jpa.dao;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import myapp.jpa.model.Person;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByFirstName(String name);

    List<Person> findByFirstNameLike(String name);

    @Transactional
    @Modifying
    @Query("DELETE FROM Person p WHERE p.firstName LIKE :pattern")
    void deleteLikeFirstName(@Param("pattern") String pattern);


}