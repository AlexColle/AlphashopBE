package com.xantrix.webapp.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Utenti {
    private String id;
    private String userId;
    private String password;
    private String attivo;

    private List<String> ruoli;
}
