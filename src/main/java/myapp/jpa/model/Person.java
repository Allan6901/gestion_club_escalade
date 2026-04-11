package myapp.jpa.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;

@Entity(name = "Person")
@Table(name = "TPerson",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "first_name", "birth_day"
                })
        })
@NamedQuery(name = "Person.findPersonsByFirstName", query = "SELECT p FROM Person p WHERE p.firstName LIKE :pattern")
@NamedQuery(
        name = "Person.findPersonsByCarModel",
        query = "SELECT DISTINCT p FROM Person p JOIN p.cars c WHERE c.model = :model"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Basic(optional = false,fetch = FetchType.EAGER)
    @Column(name="first_name",length = 200)
    private String firstName;

    @Basic(optional = true,fetch = FetchType.EAGER)
    @Column(name="second_name",length = 100,nullable = true,unique = true)
    private String secondName;

    @Embedded
    private Address address;

    @Basic()
    @Temporal(TemporalType.DATE)
    @Column(name="birth_day")
    private Date birthDay;

    @Version
    private long version = 0;

    @Transient
    public static long updateCounter = 0;

    @OneToMany(//
            // chargement retardé (optionnel)
            fetch = FetchType.LAZY,
            // utile si plusieurs liaisons personne -> voiture
            mappedBy = "owner",
            cascade = {//
                    // Modifier une personne -> modification des voitures
                    CascadeType.MERGE,
                    // Création d'une personne -> création des voitures
                    CascadeType.PERSIST,
                    // Suppression d'une personne -> suppression des voitures
                    CascadeType.REMOVE
            }//
    )
    @ToString.Exclude
    @OrderBy("immatriculation ASC")
    private Set<Car> cars;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    // @JoinTable est optionnelle (afin de préciser les tables)
    @JoinTable(
            name = "Person_Movie",
            joinColumns = { @JoinColumn(name = "id_person") },
            inverseJoinColumns = { @JoinColumn(name = "id_movie") }
    )
    @ToString.Exclude
    private Set<Movie> movies;


    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name="id_cv")
    private Cv cv;


    public Person(String firstName, String secondName, Address address, Date birthDay) {
        this(0L, firstName, secondName, address, birthDay, 0L, null,null,null);
    }

    @PreUpdate
    public void beforeUpdate() {
        System.err.println("PreUpdate of " + this);
    }

    @PostUpdate
    public void afterUpdate() {
        System.err.println("PostUpdate of " + this);
        updateCounter++;
    }


    public void addCar(Car c) {
        if (cars == null) {
            cars = new HashSet<>();
        }
        cars.add(c);
        c.setOwner(this);
    }

    public void addMovie(Movie movie) {
        if (movies == null) {
            movies = new HashSet<>();
        }
        movies.add(movie);
    }

    public void addCv(Cv cv) {
        if (cv == null) {
            cv = new Cv();
        }
    }

}