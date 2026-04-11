package myapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCode {

    @NotNull
    @Size(min = 1, max = 1)
    @Pattern(regexp = "[A-Z]", message = "Le code doit débuter par une majuscule")
    @Column(name = "code_base")
    String base;

    @Min(value = 1000, message = "Le numéro doit être >= à 1000")
    @Max(value = 9999, message = "Le numéro doit être <= à 9999")
    @Column(name = "code_number")
    int number;

}