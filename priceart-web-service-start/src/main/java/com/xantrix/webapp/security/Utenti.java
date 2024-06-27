package com.xantrix.webapp.security;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Utenti
{
    private String id;
    private String userId;
    private String password;
    private String attivo;

    private List<String> ruoli;
}
