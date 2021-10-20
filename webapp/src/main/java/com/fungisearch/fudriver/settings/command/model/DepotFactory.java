package com.fungisearch.fudriver.settings.command.model;

import com.fungisearch.fudriver.settings.command.repository.DepotRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepotFactory {

    private final DepotRepository depotRepository;
    private final BeanValidator beanValidator;

    @Autowired
    public DepotFactory(DepotRepository depotRepository, BeanValidator beanValidator) {
        this.depotRepository = depotRepository;
        this.beanValidator = beanValidator;
    }

    public Depot.DepotBuilder getBuilder() {
        return new Depot.DepotBuilder(depotRepository, beanValidator);
    }

    public Depot find(long id) {
        Depot depot = depotRepository.find(id);
        if (depot != null) {
            depot.depotRepository = depotRepository;
            depot.beanValidator = beanValidator;
        }
        return depot;
    }
}
