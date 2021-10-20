package com.fungisearch.fudriver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by marcin on 02.02.16.
 */

@Configuration
@EnableScheduling
@ComponentScan(basePackages = { "com.fungisearch.fudriver.*" })
@PropertySource("file:resources/fudriver.properties")
@EnableTransactionManagement
public class AppConfig {

    private @Value("${skup.url}") String skupUrl;
    private @Value("${supplier.nr}") String supplierNr;
    private @Value("${skup.username}") String skupUsername;
    private @Value("${skup.password}") String skupPassword;
    private @Value("${label.printer.name}") String labelPrinterName;
    private @Value("${warehouse.orders}") String warehouseOrders;

    public String getSkupUrl() {
        return skupUrl;
    }

    public String getSupplierNr() {
        return supplierNr;
    }

    public String getSkupUsername() {
        return skupUsername;
    }

    public String getSkupPassword() {
        return skupPassword;
    }
    public String getLabelPrinterName() {
        return labelPrinterName;
    }
    public String getWarehouseOrders(){ return warehouseOrders;}

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
