package myapp.servlet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InMemoryPersonService {

    final private Map<String, Person> persons;

    public InMemoryPersonService() {
        persons = Collections.synchronizedMap(new HashMap<>());
        save(new Person("100", "Paul", "paul@hello.fr"));
        save(new Person("200", "Laure", "laure@univamu.fr"));
    }

    public Person find(String id) {
        var p = persons.get(id);
        if (p == null) throw new IllegalArgumentException();
        return new Person(p);
    }

    public Collection<Person> findAll() {
        return persons.values().stream().map(p -> new Person(p)).toList();
    }

    public void save(Person p) {
        persons.put(p.getId(), new Person(p));
    }

    public void remove(String id) {
        persons.remove(id);
    }

    public Map<String, String> validate(Person p) {
        var errors = new HashMap<String, String>();
        if (p.getId() == null || p.getId().isBlank()) {
            errors.put("id", "ID incorrect");
        }
        if (p.getMail() == null || !p.getMail().matches("^[a-z0-9.]+@[a-z0-9.]+")) {
            errors.put("mail", "e-mail incorrect");
        }
        if (p.getName() == null || p.getName().isBlank()) {
            errors.put("name", "Le nom est obligatoire");
        }
        return errors;
    }

}