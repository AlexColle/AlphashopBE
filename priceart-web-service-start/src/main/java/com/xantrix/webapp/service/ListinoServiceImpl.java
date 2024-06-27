package com.xantrix.webapp.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xantrix.webapp.entity.Listini;
import com.xantrix.webapp.repository.ListinoRepository;

@Service
@Transactional
public class ListinoServiceImpl implements ListinoService
{
    @Autowired
    ListinoRepository listinoRepository;

    @Override
    public void InsListino(Listini listino)
    {
        listinoRepository.save(listino);
    }

    @Override
    public void DelListino(Listini listino)
    {
        listinoRepository.delete(listino);
    }

    @Override
    public Optional<Listini> SelById(String Id)
    {
        return listinoRepository.findById(Id);
    }


}
