package com.fungisearch.fudriver.productionOrder.command.model;

import com.fungisearch.fudriver.productionOrder.command.repository.ProductionOrderRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductionOrderFactory {
    private final ProductionOrderRepository productionOrderRepository;

    public ProductionOrderFactory(ProductionOrderRepository productionOrderRepository) {
        this.productionOrderRepository = productionOrderRepository;
    }

    public ProductionOrder.ProductionOrderBuilder getBuilder(){
        return new ProductionOrder.ProductionOrderBuilder(productionOrderRepository);
    }
}
