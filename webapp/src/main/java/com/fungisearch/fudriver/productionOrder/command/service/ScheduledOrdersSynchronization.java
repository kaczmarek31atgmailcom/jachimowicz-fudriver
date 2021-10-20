package com.fungisearch.fudriver.productionOrder.command.service;


import com.fungisearch.fudriver.config.AppConfig;
import com.fungisearch.fudriver.productionOrder.command.model.ProductionOrderFactory;
import com.fungisearch.fudriver.productionOrder.query.dto.ProductionOrderImportDto;
import com.fungisearch.fudriver.reclassification.scheduler.RemoteDataImporter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.lang.reflect.Type;


@Component
@Transactional
public class ScheduledOrdersSynchronization {

    private final AppConfig appConfig;
    private final ProductionOrderFactory productionOrderFactory;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public ScheduledOrdersSynchronization(AppConfig appConfig, ProductionOrderFactory productionOrderFactory) {
        this.appConfig = appConfig;
        this.productionOrderFactory = productionOrderFactory;
    }

    @Scheduled(fixedDelay = 50000)
    @Async
    public void synchronizeTypes() {
        importOrders();
    }

    private void importOrders(){
        if(appConfig.getWarehouseOrders().equals("yes")) {
            RemoteDataImporter remoteOrderImporter = new RemoteDataImporter(appConfig.getSkupUrl(), "/rest/productionOrder/" + appConfig.getSupplierNr(), appConfig.getSkupUsername(), appConfig.getSkupPassword());
            String ordersJson = remoteOrderImporter.getRemoteTypes();
            List<ProductionOrderImportDto> orders = convertOrdersJsonToList(ordersJson);
            orders.forEach(o -> productionOrderFactory
                    .getBuilder()
                    .creationDate(o.creationDate)
                    .dueDate(o.dueDate)
                    .deliveredAmount(o.deliveredAmount)
                    .description(o.description)
                    .dueAmount(o.dueAmount)
                    .warehouseOrderId(o.warehouseOrderId)
                    .warehouseTypeId(o.warehouseTypeId)
                    .version(o.version)
                    .description(o.description)
                    .status(o.status)
                    .build());
        }
    }


    private List<ProductionOrderImportDto> convertOrdersJsonToList(String json) {
        //Gson gson = new Gson();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Type collectionType = new TypeToken<Collection<ProductionOrderImportDto>>() {
        }.getType();
        List<ProductionOrderImportDto> orders = gson.fromJson(json, collectionType);
        return orders;
    }

}
