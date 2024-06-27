package com.xantrix.webapp.controller;

import com.xantrix.webapp.appconf.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class InfoController {
    @Autowired
    private AppConfig configuration;

    @GetMapping("/info")
    public Map<String, String> getInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("listino", configuration.getListino());

        return map;

    }
}
