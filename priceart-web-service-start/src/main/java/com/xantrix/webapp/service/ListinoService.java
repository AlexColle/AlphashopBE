package com.xantrix.webapp.service;

import java.util.Optional;

import com.xantrix.webapp.entity.Listini;

public interface ListinoService
{
    public Optional<Listini> SelById(String Id);

    public void InsListino(Listini listino);

    public void DelListino(Listini listino);
}
