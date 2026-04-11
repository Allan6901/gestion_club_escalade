package myapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
public class Sortie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;
    private String siteWeb;
    private Date date;

    @ManyToOne
    private Membre createur;

    @ManyToOne
    private Categorie categorie;

}
