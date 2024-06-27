package com.xantrix.webapp.service;

import java.math.BigDecimal;

public interface PrezzoService
{
	BigDecimal SelPromoByCodArt(String CodArt);
	
	BigDecimal SelPromoByCodArtAndFid(String CodArt);
	
	BigDecimal SelByCodArtAndCodFid(String CodArt, String CodFid);
	
	void UpdOggettoPromo(String Oggetto, Long Id);
}
