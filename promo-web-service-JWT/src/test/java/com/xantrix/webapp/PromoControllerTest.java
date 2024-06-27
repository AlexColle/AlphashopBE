package com.xantrix.webapp;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Year;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.xantrix.webapp.entities.Promo;
import com.xantrix.webapp.repository.PromoRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PromoControllerTest 
{
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private PromoRepository promoRepository;
			
	@Before
	public void setup()
	{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	//ACCERTARSI CHE LA TABELLA promo SIA VUOTA
	@Test
	public void A_testlistAllPromo() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/promo/")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent())
				.andDo(print());
	}
	
	
	String JsonData =
			" {\r\n" + 
			"        \"idPromo\": \"\",\r\n" + 
			"        \"anno\": 2019,\r\n" + 
			"        \"codice\": \"UT01\",\r\n" + 
			"        \"descrizione\": \"CAMPAGNA TEST INSERIMENTO\",\r\n" + 
			"        \"dettPromo\": [\r\n" + 
			"			{\r\n" + 
			"                \"id\": -1,\r\n" + 
			"                \"riga\": 1,\r\n" + 
			"                \"codart\": \"058310201\",\r\n" + 
			"                \"codfid\": \"\",\r\n" + 
			"                \"inizio\": \"2019-01-01\",\r\n" + 
			"                \"fine\": \"2019-12-31\",\r\n" + 
			"                \"oggetto\": \"1.99\",\r\n" + 
			"                \"isfid\": \"No\",\r\n" + 
			"                \"tipoPromo\": {\r\n" + 
			"                    \"idTipoPromo\": \"1\",\r\n" + 
			"                    \"descrizione\": \"TAGLIO PREZZO\"\r\n" + 
			"                }\r\n" + 
			"            },\r\n" + 
			"            {\r\n" + 
			"                \"id\": -1,\r\n" + 
			"                \"riga\": 2,\r\n" + 
			"                \"codart\": \"000020030\",\r\n" + 
			"                \"codfid\": \"\",\r\n" + 
			"                \"inizio\": \"2019-01-01\",\r\n" + 
			"                \"fine\": \"2019-12-31\",\r\n" + 
			"                \"oggetto\": \"0.39\",\r\n" + 
			"                \"isfid\": \"Si\",\r\n" + 
			"                \"tipoPromo\": {\r\n" + 
			"                    \"idTipoPromo\": \"1\",\r\n" + 
			"                    \"descrizione\": \"TAGLIO PREZZO\"\r\n" + 
			"                }\r\n" + 
			"            }\r\n" + 
			"        ],\r\n" + 
			"        \"depRifPromo\": [\r\n" + 
			"            {\r\n" + 
			"                \"id\": -1,\r\n" + 
			"                \"idDeposito\": 526\r\n" + 
			"            },\r\n" + 
			"            {\r\n" + 
			"                \"id\": -1,\r\n" + 
			"                \"idDeposito\": 525\r\n" + 
			"            }\r\n" + 
			"        ]\r\n" + 
			"}";
	
	
	@Test
	public void B_testCreatePromo() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.post("/api/promo/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print());
	}
	
	@Test
	public void C_testlistAllPromo() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/promo/")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1)))
				.andDo(print());
	}
	
	@Test
	public void D_testlistPromoById() throws Exception
	{
		Promo promo = promoRepository.findByAnnoAndCodice(2019, "UT01");
		
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/promo/id/" + promo.getIdPromo())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				
				.andExpect(jsonPath("$.idPromo").exists())
				.andExpect(jsonPath("$.idPromo").value(promo.getIdPromo()))
				
				//Prima riga Promozione
				.andExpect(jsonPath("$.dettPromo[0].id").exists())
				.andExpect(jsonPath("$.dettPromo[0].riga").exists())
				.andExpect(jsonPath("$.dettPromo[0].riga").value("1"))
				.andExpect(jsonPath("$.dettPromo[0].codart").exists())
				.andExpect(jsonPath("$.dettPromo[0].codart").value("058310201"))
				.andExpect(jsonPath("$.dettPromo[0].oggetto").exists())
				.andExpect(jsonPath("$.dettPromo[0].oggetto").value("1.99")) 
				.andExpect(jsonPath("$.dettPromo[0].isfid").exists())
				.andExpect(jsonPath("$.dettPromo[0].isfid").value("No")) 
				
				//Tipo Promozione
				.andExpect(jsonPath("$.dettPromo[0].tipoPromo.descrizione").exists())
				.andExpect(jsonPath("$.dettPromo[0].tipoPromo.descrizione").value("TAGLIO PREZZO")) 
				
				//Seconda riga Promozione
				.andExpect(jsonPath("$.dettPromo[1].id").exists())
				.andExpect(jsonPath("$.dettPromo[1].riga").exists())
				.andExpect(jsonPath("$.dettPromo[1].riga").value("2"))
				.andExpect(jsonPath("$.dettPromo[1].codart").exists())
				.andExpect(jsonPath("$.dettPromo[1].codart").value("000020030"))
				.andExpect(jsonPath("$.dettPromo[1].oggetto").exists())
				.andExpect(jsonPath("$.dettPromo[1].oggetto").value("0.39")) 
				.andExpect(jsonPath("$.dettPromo[1].isfid").exists())
				.andExpect(jsonPath("$.dettPromo[1].isfid").value("Si")) 
				
				.andExpect(jsonPath("$.dettPromo[1].tipoPromo.descrizione").exists())
				.andExpect(jsonPath("$.dettPromo[1].tipoPromo.descrizione").value("TAGLIO PREZZO")) 
				
				.andReturn();
	}
	
	private String GetNewData()
	{
		
	String retVal =
			
			" {\r\n" + 
			"        \"idPromo\": \"" + promoRepository.findByAnnoAndCodice(2019, "UT01").getIdPromo() + "\",\r\n" + 
			"        \"anno\": 2019,\r\n" + 
			"        \"codice\": \"UT01\",\r\n" + 
			"        \"descrizione\": \"CAMPAGNA TEST INSERIMENTO\",\r\n" + 
			"        \"dettPromo\": [\r\n" + 
			"			{\r\n" + 
			"                \"id\": -1,\r\n" + 
			"                \"riga\": 1,\r\n" + 
			"                \"codart\": \"058310201\",\r\n" + 
			"                \"codfid\": \"\",\r\n" + 
			"                \"inizio\": \"2019-01-01\",\r\n" + 
			"                \"fine\": \"2019-12-31\",\r\n" + 
			"                \"oggetto\": \"1.89\",\r\n" +  //Modificato oggetto
			"                \"isfid\": \"No\",\r\n" + 
			"                \"tipoPromo\": {\r\n" + 
			"                    \"idTipoPromo\": \"1\",\r\n" + 
			"                    \"descrizione\": \"TAGLIO PREZZO\"\r\n" + 
			"                }\r\n" + 
			"            },\r\n" + 
			"            {\r\n" + 
			"                \"id\": -1,\r\n" + 
			"                \"riga\": 2,\r\n" + 
			"                \"codart\": \"000020030\",\r\n" + 
			"                \"codfid\": \"\",\r\n" + 
			"                \"inizio\": \"2019-01-01\",\r\n" + 
			"                \"fine\": \"2019-12-31\",\r\n" + 
			"                \"oggetto\": \"0.39\",\r\n" + 
			"                \"isfid\": \"No\",\r\n" +  //Modificato isfid
			"                \"tipoPromo\": {\r\n" + 
			"                    \"idTipoPromo\": \"1\",\r\n" + 
			"                    \"descrizione\": \"TAGLIO PREZZO\"\r\n" + 
			"                }\r\n" + 
			"            }\r\n" + 
			"        ],\r\n" + 
			"        \"depRifPromo\": [\r\n" + 
			"            {\r\n" + 
			"                \"id\": -1,\r\n" + 
			"                \"idDeposito\": 526\r\n" + 
			"            },\r\n" + 
			"            {\r\n" + 
			"                \"id\": -1,\r\n" + 
			"                \"idDeposito\": 525\r\n" + 
			"            }\r\n" + 
			"        ]\r\n" + 
			"}";
	
		return retVal;
	}
	
	@Test
	public void E_testUpdatePromo() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.put("/api/promo/modifica")
				.contentType(MediaType.APPLICATION_JSON)
				.content(GetNewData())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print());
	}
	
	@Test
	public void F_listPromoByCodice() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/promo/codice?anno=2019&codice=UT01")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				
				.andExpect(jsonPath("$.idPromo").exists())
				.andExpect(jsonPath("$.idPromo").value(promoRepository.findByAnnoAndCodice(2019, "UT01").getIdPromo()))
				
				.andExpect(jsonPath("$.dettPromo[0].id").exists())
				.andExpect(jsonPath("$.dettPromo[0].riga").exists())
				.andExpect(jsonPath("$.dettPromo[0].riga").value("1"))
				.andExpect(jsonPath("$.dettPromo[0].codart").exists())
				.andExpect(jsonPath("$.dettPromo[0].codart").value("058310201"))
				.andExpect(jsonPath("$.dettPromo[0].oggetto").exists())
				.andExpect(jsonPath("$.dettPromo[0].oggetto").value("1.89")) 
				.andExpect(jsonPath("$.dettPromo[0].isfid").exists())
				.andExpect(jsonPath("$.dettPromo[0].isfid").value("No")) 
				
				.andExpect(jsonPath("$.dettPromo[0].tipoPromo.descrizione").exists())
				.andExpect(jsonPath("$.dettPromo[0].tipoPromo.descrizione").value("TAGLIO PREZZO")) 
				
				.andExpect(jsonPath("$.dettPromo[1].id").exists())
				.andExpect(jsonPath("$.dettPromo[1].riga").exists())
				.andExpect(jsonPath("$.dettPromo[1].riga").value("2"))
				.andExpect(jsonPath("$.dettPromo[1].codart").exists())
				.andExpect(jsonPath("$.dettPromo[1].codart").value("000020030"))
				.andExpect(jsonPath("$.dettPromo[1].oggetto").exists())
				.andExpect(jsonPath("$.dettPromo[1].oggetto").value("0.39")) 
				.andExpect(jsonPath("$.dettPromo[1].isfid").exists())
				.andExpect(jsonPath("$.dettPromo[1].isfid").value("No")) 
				
				.andExpect(jsonPath("$.dettPromo[1].tipoPromo.descrizione").exists())
				.andExpect(jsonPath("$.dettPromo[1].tipoPromo.descrizione").value("TAGLIO PREZZO")) 
				
				.andDo(print());
				
				
	}
	
	
	@Test
	public void G_testDeletePromo() throws Exception
	{
		Promo promo = promoRepository.findByAnnoAndCodice(2019, "UT01");
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/promo/elimina/" + promo.getIdPromo())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andDo(print());
	}
	
	int year = Year.now().getValue();
	
	String Anno = String.valueOf(year);
	String Inizio = Anno + "-01-01";
	String Fine = Anno + "-12-31";
	
	String JsonData2 = "    {\r\n" + 
			"        \"idPromo\": \"\",\r\n" + 
			"        \"anno\": " + Anno + ",\r\n" + 
			"        \"codice\": \"TEST01\",\r\n" + 
			"        \"descrizione\": \"PROMO FIDELITY ONLY YOU TEST\",\r\n" + 
			"        \"dettPromo\": [\r\n" + 
			"            {\r\n" + 
			"                \"id\": -1,\r\n" + 
			"                \"riga\": 1,\r\n" + 
			"                \"codart\": \"049477701\",\r\n" + 
			"                \"codfid\": \"67000056\",\r\n" + 
			"                \"inizio\": \"" + Inizio + "\",\r\n" + 
			"                \"fine\": \"" + Fine + "\",\r\n" + 
			"                \"oggetto\": \"2,99\",\r\n" + 
			"                \"isfid\": \"Si\",\r\n" + 
			"                \"tipoPromo\": {\r\n" + 
			"                    \"idTipoPromo\": \"1\",\r\n" + 
			"                    \"descrizione\": \"TAGLIO PREZZO\"\r\n" + 
			"                }\r\n" + 
			"            },\r\n" + 
			"            {\r\n" + 
			"                \"id\": -1,\r\n" + 
			"                \"riga\": 2,\r\n" + 
			"                \"codart\": \"058310201\",\r\n" + 
			"                \"codfid\": \"67000056\",\r\n" + 
			"                \"inizio\": \"" + Inizio + "\",\r\n" + 
			"                \"fine\": \"" + Fine + "\",\r\n" + 
			"                \"oggetto\": \"1,99\",\r\n" + 
			"                \"isfid\": \"Si\",\r\n" + 
			"                \"tipoPromo\": {\r\n" + 
			"                    \"idTipoPromo\": \"1\",\r\n" + 
			"                    \"descrizione\": \"TAGLIO PREZZO\"\r\n" + 
			"                }\r\n" + 
			"            },\r\n" + 
			"            {\r\n" + 
			"                \"id\": -1,\r\n" + 
			"                \"riga\": 3,\r\n" + 
			"                \"codart\": \"000001501\",\r\n" + 
			"                \"codfid\": \"\",\r\n" + 
			"                \"inizio\": \"" + Inizio + "\",\r\n" + 
			"                \"fine\": \"" + Fine + "\",\r\n" + 
			"                \"oggetto\": \"3,90\",\r\n" + 
			"                \"isfid\": \"Si\",\r\n" + 
			"                \"tipoPromo\": {\r\n" + 
			"                    \"idTipoPromo\": \"1\",\r\n" + 
			"                    \"descrizione\": \"TAGLIO PREZZO\"\r\n" + 
			"                }\r\n" + 
			"            },\r\n" + 
			"            {\r\n" + 
			"                \"id\": -1,\r\n" + 
			"                \"riga\": 4,\r\n" + 
			"                \"codart\": \"000001501\",\r\n" + 
			"                \"codfid\": \"\",\r\n" + 
			"                \"inizio\": \"" + Inizio + "\",\r\n" + 
			"                \"fine\": \"" + Fine + "\",\r\n" + 
			"                \"oggetto\": \"3,90\",\r\n" + 
			"                \"isfid\": \"Si\",\r\n" + 
			"                \"tipoPromo\": {\r\n" + 
			"                    \"idTipoPromo\": \"1\",\r\n" + 
			"                    \"descrizione\": \"TAGLIO PREZZO\"\r\n" + 
			"                }\r\n" + 
			"            },\r\n" + 
			"            {\r\n" + 
			"                \"id\": -1,\r\n" + 
			"                \"riga\": 5,\r\n" + 
			"                \"codart\": \"049477701\",\r\n" + 
			"                \"codfid\": \"67000056\",\r\n" + 
			"                \"inizio\": \"" + Inizio + "\",\r\n" + 
			"                \"fine\": \"" + Fine + "\",\r\n" + 
			"                \"oggetto\": \"2,89\",\r\n" + 
			"                \"isfid\": \"Si\",\r\n" + 
			"                \"tipoPromo\": {\r\n" + 
			"                    \"idTipoPromo\": \"1\",\r\n" + 
			"                    \"descrizione\": \"TAGLIO PREZZO\"\r\n" + 
			"                }\r\n" + 
			"            }\r\n" + 
			"        ],\r\n" + 
			"        \"depRifPromo\": []\r\n" + 
			"    }";
			
	@Test
	public void H_testCreatePromo2() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.post("/api/promo/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData2)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print());
	}
	
	@Test
	public void L_testListPromoActive() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/promo/active")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].idPromo").exists())
				.andExpect(jsonPath("$[0].idPromo").value(promoRepository.findByAnnoAndCodice(year, "TEST01").getIdPromo()))
				.andExpect(jsonPath("$[0].dettPromo", hasSize(5)))
				
				.andExpect(jsonPath("$[0].dettPromo[0].riga").exists())
				.andExpect(jsonPath("$[0].dettPromo[0].riga").value("1"))
				.andExpect(jsonPath("$[0].dettPromo[0].codart").exists())
				.andExpect(jsonPath("$[0].dettPromo[0].codart").value("049477701"))
				.andExpect(jsonPath("$[0].dettPromo[0].oggetto").exists())
				.andExpect(jsonPath("$[0].dettPromo[0].oggetto").value("2,99")) 
				.andExpect(jsonPath("$[0].dettPromo[0].isfid").exists())
				.andExpect(jsonPath("$[0].dettPromo[0].isfid").value("Si")) 
				.andExpect(jsonPath("$[0].dettPromo[0].descrizione").exists())
				.andExpect(jsonPath("$[0].dettPromo[0].descrizione").value("PANTE.SHAMPOO RICCI ML250")) // Descrizione ricavata dal servizio articoli
				.andExpect(jsonPath("$[0].dettPromo[0].prezzo").exists())
				.andExpect(jsonPath("$[0].dettPromo[0].prezzo").value("4.05")) // Prezzo ricavato dal servizio articoli
				
				
				.andDo(print());
	}
	
	@Test
	public void M_ErrListPromoById() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/promo/id/ABC")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.codice").value(404))
				.andExpect(jsonPath("$.messaggio").value("Promozione Assente o Id Errato"))
				.andDo(print());
	}
	
	/*
	@Test
	public void N_testDeletePromo() throws Exception
	{
		Promo promo = promoRepository.findByAnnoAndCodice(year, "TEST01");
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/promo/elimina/" + promo.getIdPromo())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andDo(print());
	}
	*/
	
}
