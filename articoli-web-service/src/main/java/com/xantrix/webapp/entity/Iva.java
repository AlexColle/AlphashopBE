package com.xantrix.webapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "IVA")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Iva {
    @Id
    @Column (name = "IDIVA")
    private int idIva;

    @Column (name = "DESCRIZIONE")
    private String descrizione;

    @Column (name = "ALIQUOTA")
    private int aliquota;

    @OneToMany (fetch = FetchType.LAZY, mappedBy = "iva")
    @JsonBackReference
    private Set<Articoli> articoli = new HashSet<>();
}
