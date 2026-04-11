package myapp.servlet;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private String id;
    private String name;
    private String mail;

    public Person(Person p) {
        this(p.id, p.name, p.mail);
    }

}