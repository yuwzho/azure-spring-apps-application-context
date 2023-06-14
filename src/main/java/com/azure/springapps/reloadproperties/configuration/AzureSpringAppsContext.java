package com.azure.springapps.reloadproperties.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "azure-spring-apps")
@Data
public class AzureSpringAppsContext {

    private String subscriptionId;
    private String resourceGroup;
    private String name;

    private AppContext app;

    private DeploymentContext deployment;

    @Data
    public static class AppContext {
        private String name;
    }

    @Data
    public static class DeploymentContext {
        private String name;
        private boolean active;
    }
}

