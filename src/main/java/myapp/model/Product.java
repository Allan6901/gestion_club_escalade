package myapp.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;
import myapp.web.Bye;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue
    private Long number;

    @NotEmpty
    @Size(min = 1, message="{product.name}")
    // avant @Size(min = 1, message = "Le nom est obligatoire")
    @Basic
    private String name;

    @NotNull
    @Min(value = 1, message = "Le prix est trop bas")
    @Basic
    private Double price;

    @NotEmpty(message = "La description est obligatoire")
    @Size(min = 1, max = 100, message = "Entre 1 et 200 caractères")
    @Bye
    @Basic
    private String description;

    @NotEmpty
    @Size(min = 1, message = "Le type doit être renseigné")
    @Basic
    private String type;

    @Valid
    @Embedded
    private ProductCode code;

}