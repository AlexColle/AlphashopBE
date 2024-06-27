package com.xantrix.webapp.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tipopromo")
@Setter
@Getter
public class TipoPromo implements Serializable
{
	private static final long serialVersionUID = 8452515756703751450L;
	
	@Id
	@Column(name = "IDTIPOPROMO")
	private int idTipoPromo;
	
	@Basic
	private String descrizione;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoPromo")
	@JsonBackReference
	private Set<DettPromo> dettPromo = new HashSet<>();
	
	public TipoPromo() {}
	
	public TipoPromo (int IdTipoPromo)
	{
		this.idTipoPromo = IdTipoPromo;
	}
	

}
