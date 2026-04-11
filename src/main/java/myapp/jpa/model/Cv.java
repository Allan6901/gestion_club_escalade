package myapp.jpa.model;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Cv {
    @Id
    private Long id;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "FIRST_NAMES",
            joinColumns = @JoinColumn(name = "person_id"))
    @Column(name = "first_name")
    Collection<String> firstNames;

    @ElementCollection(fetch = FetchType.LAZY)
    @OrderColumn(name = "position")
    List<String> nickNames;


}
