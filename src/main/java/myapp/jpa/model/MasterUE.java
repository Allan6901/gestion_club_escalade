package myapp.jpa.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MasterUE extends UE {

    @Basic
    private String masterName;

    public MasterUE(String code, int ects, String masterName) {
        super(code, ects);
        this.masterName = masterName;
    }
}