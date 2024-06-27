
package com.xantrix.webapp.UnitTest.RepositoryTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import com.xantrix.webapp.entity.DettListini;
import com.xantrix.webapp.entity.Listini;
import com.xantrix.webapp.repository.ListinoRepository;
import com.xantrix.webapp.repository.PrezziRepository;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@TestPropertySource(locations="classpath:application-list1.properties")
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = Application.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PrezziRepositoryTest
{
    @Autowired
    private PrezziRepository prezziRepository;
    
    @Autowired
    private ListinoRepository listinoRepository;
    
    String IdList = "100";
    String CodArt = "002000301";
    BigDecimal Prezzo = new BigDecimal("1.00");
    
    @Test
    public void A_testInsListino()
    {
    	Listini listinoTest = new Listini(IdList,"Listino Test 100","No");
    	
    	Set<DettListini> dettListini = new HashSet<>();
    	DettListini dettListTest = new DettListini(CodArt,Prezzo, listinoTest);
    	dettListini.add(dettListTest);
    	
    	listinoTest.setDettListini(dettListini);
    	
    	listinoRepository.save(listinoTest);
    	
    	assertThat(listinoRepository
        		.findById(IdList))
    			.isNotEmpty();
    	
    }
    
    @Test
	public void B_TestfindByCodArtAndIdList1()
	{
        
        assertThat(prezziRepository
        		.findByCodArtAndIdList(CodArt, IdList))
        		.extracting(DettListini::getPrezzo)
				.isEqualTo(Prezzo);
    }
    
    @Test
    @Transactional
	public void C_TestDeletePrezzo()
	{
        
    	prezziRepository.deleteByCodArtAndIdList(CodArt, IdList);
    	
        assertThat(prezziRepository
        		.findByCodArtAndIdList(CodArt, IdList ))
        		.isNull();
    }
    
    @Test
	public void D_TestDeleteListino()
	{
    	Optional<Listini> listinoTest = listinoRepository.findById(IdList);
    	
    	listinoRepository.delete(listinoTest.get());
    	
        assertThat(prezziRepository
        		.findByCodArtAndIdList(CodArt, IdList))
        		.isNull();
    }
    
    
}




















