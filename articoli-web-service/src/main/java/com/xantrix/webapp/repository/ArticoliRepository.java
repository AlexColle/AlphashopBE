package com.xantrix.webapp.repository;

import com.xantrix.webapp.entity.Articoli;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticoliRepository extends PagingAndSortingRepository<Articoli, String> {
    @Query (value = "SELECT DISTINCT * FROM Articoli WHERE DESCRIZIONE LIKE :desArt", nativeQuery = true)
    List<Articoli> findByDescrizioneLike(@Param("desArt") String descrizione);

    List<Articoli> findByDescrizioneLike(String descrizione, Pageable pageable);
    Articoli findByCodArt(String codArt);
}
