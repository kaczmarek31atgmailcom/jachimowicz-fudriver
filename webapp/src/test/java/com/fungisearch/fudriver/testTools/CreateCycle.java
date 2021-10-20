package com.fungisearch.fudriver.testTools;

import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import com.fungisearch.fudriver.settings.command.model.MyceliumFactory;
import com.fungisearch.fudriver.settings.command.model.SubsoilFactory;
import com.fungisearch.fudriver.testTools.UT.CreateChamber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CreateCycle {

    private final MyceliumFactory myceliumFactory;
    private final SubsoilFactory subsoilFactory;
    private final CreateChamber createChamber;
    private final CycleFactory cycleFactory;
    private final CreateUser createUser;

    @Autowired
    public CreateCycle(MyceliumFactory myceliumFactory, SubsoilFactory subsoilFactory, CreateChamber createChamber, CycleFactory cycleFactory, CreateUser createUser) {
        this.myceliumFactory = myceliumFactory;
        this.subsoilFactory = subsoilFactory;
        this.createChamber = createChamber;
        this.cycleFactory = cycleFactory;
        this.createUser = createUser;
    }

    public Cycle create() {
        return cycleFactory.getBuilder()
                .chamber(createChamber.create())
                .technologist(createUser.create())
                .mycelium(myceliumFactory.getBuilder().name("test_mycelium").build())
                .subsoil(subsoilFactory.getBuilder().name("test_subsoil").build())
                .initDate(20170101)
                .description("ala ma kota")
                .weight(123)
                .area(124)
                .humidity(23)
                .build();
    }

    public Cycle create(int startDate) {
        return cycleFactory.getBuilder()
                .chamber(createChamber.create())
                .technologist(createUser.create())
                .mycelium(myceliumFactory.getBuilder().name("test_mycelium").build())
                .subsoil(subsoilFactory.getBuilder().name("test_subsoil").build())
                .initDate(startDate)
                .description("ala ma kota")
                .weight(123)
                .area(124)
                .humidity(23)
                .build();
    }

    public Cycle create(int startDate, int startSecondPeriod) {
        return cycleFactory.getBuilder()
                .chamber(createChamber.create())
                .technologist(createUser.create())
                .mycelium(myceliumFactory.getBuilder().name("test_mycelium").build())
                .subsoil(subsoilFactory.getBuilder().name("test_subsoil").build())
                .initDate(startDate)
                .startSecondPeriod(startSecondPeriod)
                .description("ala ma kota")
                .weight(123)
                .area(124)
                .humidity(23)
                .build();
    }

    public Cycle create(int startDate, int startSecondPeriod,int startThirdPeriod) {
        return cycleFactory.getBuilder()
                .chamber(createChamber.create())
                .technologist(createUser.create())
                .mycelium(myceliumFactory.getBuilder().name("test_mycelium").build())
                .subsoil(subsoilFactory.getBuilder().name("test_subsoil").build())
                .initDate(startDate)
                .startSecondPeriod(startSecondPeriod)
                .startThirdPeriod(startThirdPeriod)
                .description("ala ma kota")
                .weight(123)
                .area(124)
                .humidity(23)
                .build();
    }
}
