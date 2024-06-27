package com.xantrix.webapp.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponse 
{
	private int codice;
	private String messaggio;
	
}
