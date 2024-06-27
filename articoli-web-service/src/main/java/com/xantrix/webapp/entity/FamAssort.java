package com.xantrix.webapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "FAMASSORT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class FamAssort {
    @Id
    @Column (name = "ID")
    private int id;

    @Column (name = "DESCRIZIONE")
    private String descrizione;

    @OneToMany (fetch = FetchType.LAZY, mappedBy = "famAssort")
    @JsonBackReference
    private Set<Articoli> articoli = new HashSet<>();
}
