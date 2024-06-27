package com.xantrix.webapp.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.math.BigDecimal;

@FeignClient(name ="PriceArtWebService", url="localhost:5071") //, configuration = FeignClientConfiguration.class)
public interface PriceClient
{
    @GetMapping(value = "/api/prezzi/{codart}")
    public BigDecimal getDefPriceArt(@RequestHeader("Authorization") String AuthHeader, @PathVariable("codart") String CodArt);

    @GetMapping(value = "/api/prezzi/{codart}/{idlist}")
    public BigDecimal getPriceArt(@RequestHeader("Authorization") String AuthHeader, @PathVariable("codart") String CodArt,
                              @PathVariable("idlist") String IdList);

    @GetMapping(value = "/api/prezzi/noauth/{codart}")
    public BigDecimal getDefPriceArt(@PathVariable("codart") String CodArt);

    @GetMapping(value = "/api/prezzi/noauth/{codart}/{idlist}")
    public BigDecimal getPriceArt(@PathVariable("codart") String CodArt, @PathVariable("idlist") String IdList);
}
