package com.xantrix.webapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
 

import com.xantrix.webapp.entities.DettPromo;
import com.xantrix.webapp.entities.Promo;
import com.xantrix.webapp.entities.TipoPromo;
import com.xantrix.webapp.repository.PrezziPromoRepository;
import com.xantrix.webapp.repository.PromoRepository;

@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = Application.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PromoRepositoryTest 
{
	@Autowired
	private PromoRepository promoRepository;
	
	@Autowired
	private PrezziPromoRepository prezziPromoRepository;
	
	int Anno = Year.now().getValue();
	
	String IdPromo = "";
	String Codice = "TEST01";
	String Descrizione = "PROMO TEST1";
	
	private static boolean isInitialized = false;
	private static boolean isTerminated = false;
	
	@Before
	public void setup() 
			throws ParseException
	{
		if (isInitialized) return;
		
		UUID uuid = UUID.randomUUID();
		IdPromo = uuid.toString();
		
		Promo promo = new Promo(IdPromo,Anno,Codice,Descrizione);
		promoRepository.save(promo);
		
		//La promo sarà valida l'intero anno corrente
		Date Inizio = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(Anno) + "-01-01");  
		Date Fine = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(Anno) + "-12-31");
		
		DettPromo dettPromo = new DettPromo();
		
		promo = promoRepository.findByAnnoAndCodice(Anno, Codice);
		
		//riga 1 promozione standard
		dettPromo.setId(-1);
		dettPromo.setInizio(Inizio);
		dettPromo.setFine(Fine);
		dettPromo.setCodart("049477701");
		dettPromo.setOggetto("1.10");
		dettPromo.setIsfid("No");
		dettPromo.setRiga(1);
		dettPromo.setTipoPromo(new TipoPromo(1));
		
		dettPromo.setPromo(promo);
		
		prezziPromoRepository.save(dettPromo);
		
		//riga 2 promozione fidelity
		dettPromo.setId(-1);
		dettPromo.setInizio(Inizio);
		dettPromo.setFine(Fine);
		dettPromo.setCodart("004590201");
		dettPromo.setOggetto("1.99");
		dettPromo.setIsfid("Si");
		dettPromo.setRiga(2);
		dettPromo.setTipoPromo(new TipoPromo(1));
		
		dettPromo.setPromo(promo);
		
		prezziPromoRepository.save(dettPromo);
		
		//riga 2 promozione fidelity Only You
		dettPromo.setId(-1);
		dettPromo.setInizio(Inizio);
		dettPromo.setFine(Fine);
		dettPromo.setCodart("008071001");
		dettPromo.setOggetto("2.19");
		dettPromo.setIsfid("Si");
		dettPromo.setCodfid("67000076");
		dettPromo.setRiga(3);
		dettPromo.setTipoPromo(new TipoPromo(1));
		
		dettPromo.setPromo(promo);
		
		prezziPromoRepository.save(dettPromo);
		
		Anno = Anno - 1; //assicuriamoci che la promo sia scaduta
		
		Inizio = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(Anno) + "-01-01");  
		Fine = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(Anno) + "-12-31");
		
		//riga 4 promozione standard scaduta
		dettPromo.setId(-1);
		dettPromo.setInizio(Inizio);
		dettPromo.setFine(Fine);
		dettPromo.setCodart("002001601");
		dettPromo.setOggetto("0.99");
		dettPromo.setRiga(4);
		dettPromo.setTipoPromo(new TipoPromo(1));
		
		dettPromo.setPromo(promo);
		
		prezziPromoRepository.save(dettPromo);
		
		isInitialized = true;
		
	}
	

	@Test
	public void A_TestSelByCodArt() 
	{
		
		assertThat(prezziPromoRepository.SelByCodArt("049477701"))
		.extracting(DettPromo::getOggetto)
		.isEqualTo("1.10");
	}
	
	@Test
	public void B_TestSelByCodArtAndFid()
	{
		assertThat(prezziPromoRepository.SelByCodArtAndFid("004590201"))
		.extracting(DettPromo::getOggetto)
		.isEqualTo("1.99");
	}
	
	@Test
	public void C_TestSelByCodArtAndCodFid()
	{
		assertThat(prezziPromoRepository.SelByCodArtAndCodFid("008071001","67000076"))
		.extracting(DettPromo::getOggetto)
		.isEqualTo("2.19");
	}
	
	
	@Test
	public void D_TestSelPromoScad() 
	{
		
		assertThat(prezziPromoRepository.SelByCodArt("002001601"))
		.isNull();
		
	}
	
	@Test
	public void E_TestfindByIdPromo()
	{
		
		String IdPromo = promoRepository.findByAnnoAndCodice(Anno, Codice).getIdPromo();
		
		assertThat(promoRepository.findById(IdPromo).get())
		.extracting(Promo::getDescrizione)
		.isEqualTo(Descrizione);
	}
	
	@Test
	public void F_TestfindByAnnoAndCodice() 
	{
		
		assertThat(promoRepository.findByAnnoAndCodice(Anno, Codice))
		.extracting(Promo::getDescrizione)
		.isEqualTo(Descrizione);
		
	}
	
	@Test
	public void G_TestSelPromoActive() 
	{
		assertThat(promoRepository.SelPromoActive(), hasSize(1));
		
		//Se si aggiungono altri test spostare nell'ultimo in linea cronologica
		isTerminated = true;
	}
	
	
	
	@After
	public void DelPromo()
	{
		if (isTerminated)
			promoRepository.delete(promoRepository.findByAnnoAndCodice(Anno, Codice));
	}
	
}
