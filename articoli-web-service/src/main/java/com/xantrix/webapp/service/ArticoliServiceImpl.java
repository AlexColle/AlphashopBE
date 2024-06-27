package com.xantrix.webapp.service;

import com.xantrix.webapp.entity.Articoli;
import com.xantrix.webapp.repository.ArticoliRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class ArticoliServiceImpl implements ArticoliService {

    @Autowired
    ArticoliRepository articoliRepository;

    @Override
    public Iterable<Articoli> SelTutti() {
        return articoliRepository.findAll();
    }

    @Override
    public List<Articoli> SelByDescrizione(String descrizione, Pageable pageable) {
        return articoliRepository.findByDescrizioneLike( descrizione, pageable);
    }

    @Override
    public List<Articoli> SelByDescrizione(String descrizione) {
        return articoliRepository.findByDescrizioneLike(descrizione);
    }

    @Override
    public Articoli SelByCodArt(String codArt) {
        if(codArt!=null){
            Articoli articolo = articoliRepository.findByCodArt(codArt);
            if(articolo!=null){
                return articolo;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public void DelArticolo (Articoli articolo) {
        articoliRepository.delete( articolo );
    }

    @Override
    @Transactional
    public void InsArticolo (Articoli articolo) {
        articoliRepository.save( articolo );
    }

}
