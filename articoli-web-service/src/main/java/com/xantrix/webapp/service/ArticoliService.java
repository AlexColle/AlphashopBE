package com.xantrix.webapp.service;

import com.xantrix.webapp.entity.Articoli;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ArticoliService {
    public Iterable<Articoli> SelTutti();

    public List<Articoli> SelByDescrizione (String descrizione);

    public List<Articoli> SelByDescrizione (String descrizione, Pageable pageable);

    public Articoli SelByCodArt (String codArt);

    public void DelArticolo(Articoli articolo);

    public void InsArticolo(Articoli articolo);
}
