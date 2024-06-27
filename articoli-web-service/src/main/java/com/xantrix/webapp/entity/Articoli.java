package com.xantrix.webapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "articoli")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Articoli implements Serializable {
    private static final long serialVersionUID = 291353626011036772L;

    @Id
    @Column (name = "CODART")
    @Size(min = 5, max = 20, message = "{Size.Articoli.codArt.Validation}")
    @NotNull(message = "{NotNull.Articoli.codArt.Validation}")
    private String codArt;

    @Column (name = "DESCRIZIONE")
    @Size(min = 6, max = 80, message = "{Size.Articoli.descrizione.Validation}")
    private String descrizione;

    @Column (name = "UM")
    private String um;

    @Column (name = "CODSTAT")
    private String codStat;

    @Column (name = "PZCART")
    @Max(value = 99, message = "{Size.Articoli.pzCart.Validation}")
    private Integer pzCart;

    @Column (name = "PESONETTO")
    @Min(value = (long) 0.01, message = "{Size.Articoli.pesoNetto.Validation}")
    private Double pesoNetto;

    @Column (name = "IDSTATOART")
    @NotNull(message = "{Size.Articoli.idStatoArt.Validation}")
    private String idStatoArt;

    @Temporal(TemporalType.DATE)
    @Column (name = "DATACREAZIONE")
    private Date dataCreaz;

    @Transient
    private BigDecimal prezzo;

    @Transient
    private BigDecimal promo;

    @OneToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "articolo", orphanRemoval = true)
    @JsonManagedReference
    private Set<Barcode> barcode = new HashSet<>();

    @OneToOne (fetch = FetchType.LAZY, mappedBy = "articolo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Ingredienti ingredienti;

    @ManyToOne
    @JoinColumn (name = "IDIVA", referencedColumnName = "idIva")
    private Iva iva;

    @ManyToOne
    @JoinColumn (name = "IDFAMASS", referencedColumnName = "id")
    private FamAssort famAssort;
}
