package com.xantrix.webapp.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class ErrorResponse
{
    private Date data = new Date();
    private int codice;
    private String messaggio;
}
