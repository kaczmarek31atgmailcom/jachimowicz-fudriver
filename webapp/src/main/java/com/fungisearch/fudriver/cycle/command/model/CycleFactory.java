package com.fungisearch.fudriver.cycle.command.model;


import com.fungisearch.fudriver.cycle.command.repository.CycleRepository;
import com.fungisearch.fudriver.settings.command.repository.ChamberRepository;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import com.fungisearch.fudriver.user.query.service.UserService;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CycleFactory {

    private final CycleRepository cycleRepository;
    private final BeanValidator beanValidator;
    private final ZarobkiFactory zarobkiFactory;
    private final UserService userService;
    private final ChamberRepository chamberRepository;
    private final UserFactory userFactory;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    public CycleFactory(CycleRepository cycleRepository, BeanValidator beanValidator, ZarobkiFactory zarobkiFactory, UserService userService, ChamberRepository chamberRepository, UserFactory userFactory) {
        this.cycleRepository = cycleRepository;
        this.beanValidator = beanValidator;
        this.zarobkiFactory = zarobkiFactory;
        this.userService = userService;
        this.chamberRepository = chamberRepository;
        this.userFactory = userFactory;
    }

    public Cycle.CycleBuilder getBuilder() {
        return new Cycle.CycleBuilder(cycleRepository, beanValidator, zarobkiFactory);
    }

    public Cycle find(long id) {
        Cycle cycle = cycleRepository.find(id);
        cycle.cycleRepository = cycleRepository;
        cycle.beanValidator = beanValidator;
        cycle.zarobkiFactory = zarobkiFactory;
        return cycle;
    }

    public Cycle findByChamberId(long chamberId, int startDate) {
        Cycle cycle = cycleRepository.findOpenCycleByChamberId(chamberId);
        if (cycle == null) {
            cycle = new Cycle.CycleBuilder(cycleRepository, beanValidator, zarobkiFactory)
                    .chamber(chamberRepository.find(chamberId))
                    .technologist(userFactory.find(userService.getCurrentUserId()))
                    .initDate(startDate)
                    .build();
        } else {
            cycle.cycleRepository = cycleRepository;
            cycle.zarobkiFactory = zarobkiFactory;
            cycle.beanValidator = beanValidator;
            cycle.edit(new Cycle.Edit()
            .initDate(startDate));
        }
        return cycle;
    }


}
