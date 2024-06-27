package com.xantrix.webapp.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xantrix.webapp.entity.Articoli;
import com.xantrix.webapp.entity.Barcode;
import com.xantrix.webapp.exception.BindingException;
import com.xantrix.webapp.exception.DuplicateException;
import com.xantrix.webapp.exception.NotFoundException;
import com.xantrix.webapp.service.ArticoliService;
import com.xantrix.webapp.service.BarcodeService;

@RestController
@RequestMapping("api/articoli")
//@CrossOrigin(origins="http://localhost:4200")
public class ArticoliController
{
    private static final Logger logger = LoggerFactory.getLogger(ArticoliController.class);

    @Autowired
    private ArticoliService articoliService;

    @Autowired
    private BarcodeService barcodeService;

    @Autowired
    private PriceClient priceClient;

    @Autowired
    private PromoClient promoClient;

    @Autowired
    private ResourceBundleMessageSource errMessage;

    private BigDecimal getPriceArt(String CodArt, String IdList, String Header)
    {

        BigDecimal Prezzo = (IdList.length() > 0) ?
                priceClient.getPriceArt(Header, CodArt, IdList) :
                priceClient.getDefPriceArt(Header, CodArt);

        logger.info("Prezzo Articolo " + CodArt + ": " + Prezzo);

        return Prezzo;
    }

    private BigDecimal getPriceArt(String CodArt, String IdList)
    {

        BigDecimal Prezzo = (IdList.length() > 0) ?
                priceClient.getPriceArt(CodArt, IdList) :
                priceClient.getDefPriceArt(CodArt);

        logger.info("Prezzo Articolo " + CodArt + ": " + Prezzo);

        return Prezzo;
    }

    private BigDecimal getPromoArt(String CodArt, String Header)
    {

        BigDecimal Prezzo = promoClient.getPromoArt(Header, CodArt);

        logger.info("Prezzo Promo Articolo " + CodArt + ": " + Prezzo);

        return Prezzo;
    }

    @GetMapping(value = "/test")
    public ResponseEntity<?> testConnex()
    {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();

        responseNode.put("code", HttpStatus.OK.toString());
        responseNode.put("message", "Test Connessione Ok");

        return new ResponseEntity<>(responseNode, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(value = "/noauth/cerca/codice/{codart}", produces = "application/json")
    public ResponseEntity<Articoli> GetArtNoAuth(@PathVariable("codart") String CodArt)
            throws NotFoundException
    {
        logger.info("****** Otteniamo l'articolo con codice " + CodArt + " *******");

        Articoli articolo = articoliService.SelByCodArt(CodArt);

        if (articolo == null)
        {
            String ErrMsg = String.format("L'articolo con codice %s non è stato trovato!", CodArt);

            logger.warn(ErrMsg);

            throw new NotFoundException(ErrMsg);
        }
        else
        {
            articolo.setPrezzo(this.getPriceArt(articolo.getCodArt(), ""));
        }

        return new ResponseEntity<>(articolo, HttpStatus.OK);
    }

    // ------------------- Ricerca Per Barcode ------------------------------------
    @GetMapping(value = "/cerca/ean/{barcode}", produces = "application/json")
    public ResponseEntity<Articoli> listArtByEan(@PathVariable("barcode") String Barcode, HttpServletRequest httpRequest)
            throws NotFoundException
    {
        logger.info("****** Otteniamo l'articolo con barcode " + Barcode + " *******");

        String AuthHeader = httpRequest.getHeader("Authorization");

        Articoli articolo;
        Barcode Ean = barcodeService.SelByBarcode(Barcode);

        if (Ean == null)
        {
            String ErrMsg = String.format("Il barcode %s non è stato trovato!", Barcode);

            logger.warn(ErrMsg);

            throw new NotFoundException(ErrMsg);
            //return new ResponseEntity<Articoli>(HttpStatus.NOT_FOUND);
        }
        else
        {
            articolo = Ean.getArticolo();
            articolo.setPrezzo(this.getPriceArt(articolo.getCodArt(),"",AuthHeader));
            articolo.setPromo(this.getPromoArt(articolo.getCodArt(), AuthHeader));
        }

        return new ResponseEntity<Articoli>(articolo, HttpStatus.OK);

    }

    // ------------------- Ricerca Per Codice ------------------------------------
    @GetMapping(value = "/cerca/codice/{codart}", produces = "application/json")
    public ResponseEntity<Articoli> listArtByCodArt(@PathVariable("codart") String CodArt, HttpServletRequest httpRequest)
            throws NotFoundException
    {
        logger.info("****** Otteniamo l'articolo con codice " + CodArt + " *******");

        String AuthHeader = httpRequest.getHeader("Authorization");

        Articoli articolo = articoliService.SelByCodArt(CodArt);

        if (articolo == null)
        {
            String ErrMsg = String.format("L'articolo con codice %s non è stato trovato!", CodArt);

            logger.warn(ErrMsg);

            throw new NotFoundException(ErrMsg);
        }
        else
        {
            articolo.setPrezzo(this.getPriceArt(articolo.getCodArt(), "", AuthHeader));
            articolo.setPromo(this.getPromoArt(articolo.getCodArt(), AuthHeader));
        }

        return new ResponseEntity<Articoli>(articolo, HttpStatus.OK);
    }

    // ------------------- Ricerca Per Descrizione ------------------------------------
    @GetMapping(value = "/cerca/descrizione/{filter}", produces = "application/json")
    public ResponseEntity<List<Articoli>> listArtByDesc(@PathVariable("filter") String Filter, HttpServletRequest httpRequest)
            throws NotFoundException
    {
        logger.info("****** Otteniamo gli articoli con Descrizione: " + Filter + " *******");

        String AuthHeader = httpRequest.getHeader("Authorization");

        List<Articoli> articoli = articoliService.SelByDescrizione(Filter.toUpperCase() + "%");

        if (articoli == null)
        {
            String ErrMsg = String.format("Non è stato trovato alcun articolo avente descrizione %s", Filter);

            logger.warn(ErrMsg);

            throw new NotFoundException(ErrMsg);

        }
        else
        {
            articoli.forEach(f -> f.setPrezzo(this.getPriceArt(f.getCodArt(), "", AuthHeader)));
            articoli.forEach(f -> f.setPromo(this.getPromoArt(f.getCodArt(), AuthHeader)));
        }

        return new ResponseEntity<List<Articoli>>(articoli, HttpStatus.OK);
    }

    // ------------------- INSERIMENTO ARTICOLO ------------------------------------
    @PostMapping(value = "/inserisci", produces = "application/json")
    public ResponseEntity<?> createArt(@Valid @RequestBody Articoli articolo, BindingResult bindingResult)
            throws BindingException, DuplicateException
    {
        logger.info("Salviamo l'articolo con codice " + articolo.getCodArt());

        //controllo validità dati articolo
        if (bindingResult.hasErrors())
        {
            String MsgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());

            logger.warn(MsgErr);

            throw new BindingException(MsgErr);
        }

        //Disabilitare se si vuole gestire anche la modifica
        Articoli checkArt =  articoliService.SelByCodArt(articolo.getCodArt());

        if (checkArt != null)
        {
            String MsgErr = String.format("Articolo %s presente in anagrafica! "
                    + "Impossibile utilizzare il metodo POST", articolo.getCodArt());

            logger.warn(MsgErr);

            throw new DuplicateException(MsgErr);
        }

        articoliService.InsArticolo(articolo);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();

        responseNode.put("code", HttpStatus.OK.toString());
        responseNode.put("message", "Inserimento Articolo " + articolo.getCodArt() + " Eseguita Con Successo");

        return new ResponseEntity<>(responseNode, new HttpHeaders(), HttpStatus.CREATED);
    }

    // ------------------- MODIFICA ARTICOLO ------------------------------------
    @RequestMapping(value = "/modifica", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateArt(@Valid @RequestBody Articoli articolo, BindingResult bindingResult)
            throws BindingException,NotFoundException
    {
        logger.info("Modifichiamo l'articolo con codice " + articolo.getCodArt());

        if (bindingResult.hasErrors())
        {
            String MsgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());

            logger.warn(MsgErr);

            throw new BindingException(MsgErr);
        }

        Articoli checkArt =  articoliService.SelByCodArt(articolo.getCodArt());

        if (checkArt == null)
        {
            String MsgErr = String.format("Articolo %s non presente in anagrafica! "
                    + "Impossibile utilizzare il metodo PUT", articolo.getCodArt());

            logger.warn(MsgErr);

            throw new NotFoundException(MsgErr);
        }

        articoliService.InsArticolo(articolo);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();

        responseNode.put("code", HttpStatus.OK.toString());
        responseNode.put("message", "Modifica Articolo " + articolo.getCodArt() + " Eseguita Con Successo");

        return new ResponseEntity<>(responseNode, new HttpHeaders(), HttpStatus.CREATED);
    }

    // ------------------- ELIMINAZIONE ARTICOLO ------------------------------------
    @RequestMapping(value = "/elimina/{codart}", method = RequestMethod.DELETE, produces = "application/json" )
    public ResponseEntity<?> deleteArt(@PathVariable("codart") String CodArt)
            throws NotFoundException
    {
        logger.info("Eliminiamo l'articolo con codice " + CodArt);

        Articoli articolo = articoliService.SelByCodArt(CodArt);

        if (articolo == null)
        {
            String MsgErr = String.format("Articolo %s non presente in anagrafica!",CodArt);

            logger.warn(MsgErr);

            throw new NotFoundException(MsgErr);
        }

        articoliService.DelArticolo(articolo);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();

        responseNode.put("code", HttpStatus.OK.toString());
        responseNode.put("message", "Eliminazione Articolo " + CodArt + " Eseguita Con Successo");

        return new ResponseEntity<>(responseNode, new HttpHeaders(), HttpStatus.OK);

    }





}
