package com.fungisearch.fudriver.config.query.service;

import com.fungisearch.fudriver.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by marcin on 02.04.16.
 */
@RestController
public class ConfigRestController {

    @Autowired
    private AppConfig appConfig;

    @RequestMapping(value = "/rest/config/supplierId", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    String getSupplierId() {
        return appConfig.getSupplierNr();
    }

    @GetMapping(value="/rest/config/warehouseOrders")
        public String enabledWarehouseOrders() {return appConfig.getWarehouseOrders(); }
}
