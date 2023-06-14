package com.azure.springapps.reloadproperties.controllers;

import com.azure.springapps.reloadproperties.configuration.AzureSpringAppsContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.web.bind.annotation.RestController
@Slf4j
public class RestController {

    @Autowired
    private AzureSpringAppsContext azureContext;

    @GetMapping("/hello")
    public String hello() {
        log.info("called");
        return  "Context ID: " + System.identityHashCode(azureContext) +"    Context info: "+ azureContext.toString();
    }

}
