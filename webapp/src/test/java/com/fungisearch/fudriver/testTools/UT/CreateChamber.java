package com.fungisearch.fudriver.testTools.UT;


import com.fungisearch.fudriver.settings.command.model.Chamber;
import com.fungisearch.fudriver.settings.command.model.ChamberFactory;
import com.fungisearch.fudriver.testTools.DepotITestFactory;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateChamber {

    private final ChamberFactory chamberFactory;
    private final BeanValidator beanValidator;
    private final DepotITestFactory depotITestFactory;

    @Autowired
    public CreateChamber(ChamberFactory chamberFactory, BeanValidator beanValidator, DepotITestFactory depotITestFactory) {
        this.chamberFactory = chamberFactory;
        this.beanValidator = beanValidator;
        this.depotITestFactory = depotITestFactory;
    }

    public Chamber create() {
        return chamberFactory.getBuilder()
                .depot(depotITestFactory.create())
                .area(123)
                .name("test_depot")
                .build();
    }
}
