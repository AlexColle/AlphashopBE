package com.xantrix.webapp.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dettpromo")
@Setter
@Getter
public class DettPromo implements Serializable
{
	private static final long serialVersionUID = 7444232363326102441L;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Basic(optional = false)
	private int riga;
	
	@Basic(optional = false)
	private String codart;
	
	@Transient
	private String descrizione;
	
	@Transient
	private BigDecimal prezzo;
	
	@Basic
	private String codfid;
		
	@Column(name = "INIZIO")
	@Temporal(TemporalType.DATE)
	private Date inizio;
	
	@Column(name = "FINE")
	@Temporal(TemporalType.DATE)
	private Date fine;
	
	@Basic(optional = false)
	private String oggetto;
	
	@Basic
	private String isfid;
	
	@ManyToOne
	@EqualsAndHashCode.Exclude
	@JoinColumn(name = "IDPROMO", referencedColumnName = "idPromo")
	@JsonBackReference 
	private Promo promo;
	
	@ManyToOne
	@EqualsAndHashCode.Exclude
	@JoinColumn(name = "IDTIPOPROMO", referencedColumnName = "idTipoPromo")
	private TipoPromo tipoPromo;

}
