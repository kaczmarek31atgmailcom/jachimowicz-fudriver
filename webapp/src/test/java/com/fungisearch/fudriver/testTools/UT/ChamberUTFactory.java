package com.fungisearch.fudriver.testTools.UT;

import com.fungisearch.fudriver.settings.command.model.Chamber;
import com.fungisearch.fudriver.settings.command.model.Depot;
import com.fungisearch.fudriver.settings.command.repository.ChamberRepository;
import com.fungisearch.fudriver.settings.command.repository.DepotRepository;
import com.fungisearch.fudriver.validation.BeanValidator;


public class ChamberUTFactory {

    private final ChamberRepository chamberRepository;
    private final DepotRepository depotRepository;
    private final BeanValidator beanValidator;

    public ChamberUTFactory(ChamberRepository chamberRepository, DepotRepository depotRepository, BeanValidator beanValidator) {
        this.chamberRepository = chamberRepository;
        this.depotRepository = depotRepository;
        this.beanValidator = beanValidator;
    }

    public Chamber create(){
        Depot depot = new Depot.DepotBuilder(depotRepository,beanValidator).name("depot").build();
        depot.setId(1L);
        Chamber chamber = new Chamber.ChamberBuilder(chamberRepository,beanValidator)
                .depot(depot)
                .area(123)
                .name("chamber")
                .build();
        chamber.setId(1L);
        return chamber;
    }

    public Chamber create(int area){
        Depot depot = new Depot.DepotBuilder(depotRepository,beanValidator).name("depot").build();
        depot.setId(1L);
        Chamber chamber = new Chamber.ChamberBuilder(chamberRepository,beanValidator)
                .depot(depot)
                .area(area)
                .name("chamber")
                .build();
        chamber.setId(1L);
        return chamber;
    }

}
