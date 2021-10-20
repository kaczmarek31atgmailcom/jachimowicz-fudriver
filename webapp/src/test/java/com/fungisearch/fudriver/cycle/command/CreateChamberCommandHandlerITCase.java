package com.fungisearch.fudriver.cycle.command;

import com.fungisearch.fudriver.settings.command.chamberCommands.CreateChamberCommand;
import com.fungisearch.fudriver.settings.command.chamberCommands.CreateChamberCommandHandler;
import com.fungisearch.fudriver.settings.command.model.Chamber;
import com.fungisearch.fudriver.settings.command.model.Depot;
import com.fungisearch.fudriver.settings.command.repository.ChamberRepository;
import com.fungisearch.fudriver.testTools.DepotITestFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


@ContextConfiguration(locations = {"/test-spring.xml"})
public class CreateChamberCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CreateChamberCommandHandler handler;

    @Autowired
    private ChamberRepository chamberRepository;
    @Autowired
    private DepotITestFactory depotITestFactory;

    @Test
    public void shouldCreateChamber(){
        //Given
        CreateChamberCommand command = new CreateChamberCommand();
        Depot depot =depotITestFactory.create();
        command.name = "Hala 11";
        command.area = 123;
        command.depotId = depot.getId();
        //When
        long id = handler.handle(command).entityId;
        Chamber tested = chamberRepository.find(id);
        //Then
        assertEquals(command.name,tested.getName());
        assertEquals(command.area, tested.getArea());
        assertEquals((Long)command.depotId,tested.getDepot().getId());
        assertEquals(tested,new ArrayList(depot.getChambers()).get(0));
    }
}