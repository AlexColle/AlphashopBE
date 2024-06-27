package com.xantrix.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xantrix.webapp.appconf.AppConfig;
import com.xantrix.webapp.entity.DettListini;
import com.xantrix.webapp.service.PrezziService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/prezzi")
public class PrezziController {
    private static final Logger logger = LoggerFactory.getLogger(PrezziController.class);

    @Autowired
    private PrezziService prezziService;

    @Autowired
    private AppConfig config;

    // --------------SELECT PREZZO CODART -----------------
    @GetMapping(value = {"/{codart}/{idList}","/{codart}"})
    public BigDecimal getPriceCodArt(@PathVariable("codart") String codart, @PathVariable("idList")Optional<String> optIdList) {
        BigDecimal retVal = new BigDecimal("0");
        String IdList = optIdList.isPresent() ? optIdList.get() : config.getListino();
        logger.info("Listino di Riferimento: " + IdList);
        DettListini prezzo = prezziService.SelPrezzo(codart, IdList);
        if (prezzo != null) {
            logger.info("Prezzo Articolo: " + prezzo.getPrezzo());
            retVal = prezzo.getPrezzo();
        }else {
            logger.warn("Prezzo Articolo Assente!");
        }
        return retVal;
    }

    // ---------------DELETE PREZZO LISTINO ------------------
    @RequestMapping(value = "/elimina/{codart}/{idList}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePrice(@PathVariable("codart") String codart, @PathVariable("idList")String idList) {
        logger.info(String.format("Eliminazione prezzo listino %s dell'articolo %s", idList, codart));

        HttpHeaders headers = new HttpHeaders();
        ObjectMapper mapper = new ObjectMapper();

        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectNode responseNode = mapper.createObjectNode();

        prezziService.DelPrezzo(codart, idList);

        responseNode.put("code", HttpStatus.OK.toString());
        responseNode.put("message", "Eliminazione prezzo eseguita con successo!");

        logger.info("Eliminazione prezzo eseguita con successo!");
        return new ResponseEntity<>(responseNode, headers, HttpStatus.OK);
    }
}
