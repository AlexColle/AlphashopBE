package com.xantrix.webapp.controller;

 
import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 
import com.xantrix.webapp.service.PrezzoService;


@RestController
@RequestMapping(value = "api/promo/prezzo")
public class PrezzoController 
{
	private static final Logger logger = LoggerFactory.getLogger(PrezzoController.class);
	
	@Autowired
	private PrezzoService promoService;
	
	// ------------------- SELEZIONE PREZZO PROMO ------------------------------------
	@GetMapping(value = {"/{codart}/{codfid}", "/{codart}"})
	public BigDecimal getPricePromo(@PathVariable("codart") String CodArt, @PathVariable("codfid") Optional<String> optCodFid)
	{
		BigDecimal retVal;
		
		if (optCodFid.isPresent())
		{
			
			logger.info(String.format("Cerchiamo promo riservata all fidelity %s dell'articolo %s ",optCodFid,CodArt));
			
			retVal = promoService.SelByCodArtAndCodFid(CodArt, optCodFid.get());
		}
		else 
		{
			logger.info(String.format("Cerchiamo Prezzo in promo articolo %s ",CodArt));
			
			retVal = promoService.SelPromoByCodArt(CodArt);
		}
		
		return retVal;
	}
	
	// ------------------- SELEZIONE PREZZO PROMO FIDELITY ------------------------------------
	@GetMapping(value = {"/fidelity/{codart}"})
	public BigDecimal getPricePromoFid(@PathVariable("codart") String CodArt)
	{
		BigDecimal retVal;
		
		logger.info(String.format("Cerchiamo promo fidelity articolo %s ",CodArt));
			
		retVal = promoService.SelPromoByCodArtAndFid(CodArt);
		
		return retVal;
	}
	
	
}
