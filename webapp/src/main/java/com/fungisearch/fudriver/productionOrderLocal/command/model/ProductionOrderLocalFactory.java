package com.fungisearch.fudriver.productionOrderLocal.command.model;

import com.fungisearch.fudriver.productionOrderLocal.command.repository.ProductionOrderLocalRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductionOrderLocalFactory {
    private final ProductionOrderLocalRepository productionOrderLocalRepository;

    public ProductionOrderLocalFactory(ProductionOrderLocalRepository productionOrderLocalRepository) {
        this.productionOrderLocalRepository = productionOrderLocalRepository;
    }

    public ProductionOrderLocal.ProductionOrderLocalBuilder getBuilder(){
        return new ProductionOrderLocal.ProductionOrderLocalBuilder(productionOrderLocalRepository);
    }
}
