package com.fungisearch.fudriver.testTools;

import com.fungisearch.fudriver.settings.command.model.Depot;
import com.fungisearch.fudriver.settings.command.repository.DepotRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepotITestFactory {

    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private BeanValidator beanValidator;

    public Depot create() {
        return new Depot.DepotBuilder(depotRepository, beanValidator)
                .name("test_depot")
                .build();
    }

    public Depot create(String name) {
        return new Depot.DepotBuilder(depotRepository, beanValidator)
                .name(name)
                .build();
    }
}
