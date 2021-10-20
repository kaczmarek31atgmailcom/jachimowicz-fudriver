package com.fungisearch.fudriver.settings.command.model;

import com.fungisearch.fudriver.settings.command.repository.ChamberRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChamberFactory {

    private final ChamberRepository chamberRepository;
    private final BeanValidator beanValidator;

    @Autowired
    public ChamberFactory(ChamberRepository chamberRepository, BeanValidator beanValidator) {
        this.chamberRepository = chamberRepository;
        this.beanValidator = beanValidator;
    }

    public Chamber.ChamberBuilder getBuilder() {
        return new Chamber.ChamberBuilder(chamberRepository, beanValidator);
    }

    public Chamber find(long id) {
        Chamber chamber = chamberRepository.find(id);
        chamber.chamberRepository = chamberRepository;
        chamber.beanValidator = beanValidator;
        return chamber;
    }
}
