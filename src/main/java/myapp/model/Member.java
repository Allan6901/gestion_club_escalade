package myapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"trips", "registeredTrips"})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lastName;
    private String firstName;
    private String email;
    private String password;
    private String role = "MEMBER"; // ADMIN ou MEMBER

    @OneToMany(mappedBy = "creator")
    private List<Trip> trips = new ArrayList<>();

    @ManyToMany(mappedBy = "participants")
    private List<Trip> registeredTrips = new ArrayList<>();

}
