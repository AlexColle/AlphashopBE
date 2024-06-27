package com.xantrix.webapp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "listini")
@Setter
@Getter
public class Listini implements Serializable {
    private static final long serialVersionUID = 1891268953233014092L;

    @Id
    @Column(name = "id")
    private String id;

    @Size(min = 10, max = 30, message = "{Size.Listini.descrizione.Validation}")
    @Basic
    private String descrizione;

    @Basic
    private String obsoleto;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "listino")
    @JsonManagedReference
    private Set<DettListini> dettListini = new HashSet<>();

    public Listini() {}

    public Listini(String id, String descrizione, String obsoleto) {
        this.id = id;
        this.descrizione = descrizione;
        this.obsoleto = obsoleto;
    }
}
