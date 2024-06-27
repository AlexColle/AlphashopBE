package com.xantrix.webapp.service;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.xantrix.webapp.entities.DettPromo;
import com.xantrix.webapp.repository.PrezziPromoRepository;

import java.math.BigDecimal;

@Service
@Transactional
public class PrezzoServiceImpl implements PrezzoService
{
	private static final Logger logger = LoggerFactory.getLogger(PrezzoService.class);

	@Autowired
	PrezziPromoRepository prezziPromoRep;

	@Override
	public BigDecimal SelPromoByCodArt(String CodArt)
	{
		BigDecimal retVal = BigDecimal.valueOf(0);

		DettPromo promo =  prezziPromoRep.SelByCodArt(CodArt);
		
		if (promo != null)
		{
			if (promo.getTipoPromo().getIdTipoPromo() == 1)
			{
				try
				{
					retVal = BigDecimal.valueOf(Double.parseDouble(promo.getOggetto().replace(",", ".")));
				}
				catch(NumberFormatException ex)
				{
					logger.warn(ex.getMessage());
				}
			}
			else  //TODO Gestire gli altri tipi di promozione
			{
				retVal = BigDecimal.valueOf(0);
			}
		}
		else
		{
			logger.warn("Promo Articolo Assente!!");
		}

		return retVal;
	}
	
	@Override
	public BigDecimal SelPromoByCodArtAndFid(String CodArt)
	{
		BigDecimal retVal = BigDecimal.valueOf(0);

		DettPromo promo =  prezziPromoRep.SelByCodArtAndFid(CodArt);
		
		if (promo != null)
		{
			if (promo.getTipoPromo().getIdTipoPromo() == 1)
			{
				try
				{
					retVal = BigDecimal.valueOf(Double.parseDouble(promo.getOggetto().replace(",", ".")));
				}
				catch(NumberFormatException ex)
				{
					logger.warn(ex.getMessage());
				}
			}
			else  //TODO Gestire gli altri tipi di promozione
			{
				retVal = BigDecimal.valueOf(0);
			}
		}
		else
		{
			logger.warn("Promo Articolo Fidelity Assente!!");
		}

		return retVal;
	}
	
	@Override
	public BigDecimal SelByCodArtAndCodFid(String CodArt, String CodFid)
	{
		BigDecimal retVal = BigDecimal.valueOf(0);

		DettPromo promo =  prezziPromoRep.SelByCodArtAndCodFid(CodArt, CodFid);
		
		if (promo != null)
		{
			if (promo.getTipoPromo().getIdTipoPromo() == 1)
			{
				try
				{
					retVal = BigDecimal.valueOf(Double.parseDouble(promo.getOggetto().replace(",", ".")));
				}
				catch(NumberFormatException ex)
				{
					logger.warn(ex.getMessage());
				}
			}
		}
		else
		{
			logger.warn(String.format("Promo Riservata Fidelity %s Assente!!", CodFid) );
		}

		return retVal;
	}
	
	@Override
	public void UpdOggettoPromo(String Oggetto, Long Id) 
	{
		// TODO Auto-generated method stub
	}
	
}
