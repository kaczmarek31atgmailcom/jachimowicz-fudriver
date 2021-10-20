package com.fungisearch.fudriver.cycle.command;

import com.fungisearch.fudriver.common.CreateFakeAuthentication;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import com.fungisearch.fudriver.testTools.UT.CreateChamber;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import com.fungisearch.fudriver.user.query.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(locations = {"/test-spring.xml"})
public class EditCycleHumidityCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    EditCycleHumidityCommandHandler handler;
    @Autowired
    UserService userService;
    @Autowired
    UserFactory userFactory;
    @Autowired
    CreateChamber createChamber;
    @Autowired
    CycleFactory cycleFactory;

    @Before
    public void setUp() {
        CreateFakeAuthentication.authenticatate();
    }

    @Test
    public void shouldCreateValidCommandResult() {
        //Given
        Cycle cycle = createCycle();
        EditCycleHumidityCommand command = new EditCycleHumidityCommand();
        command.cycleId = cycle.getId();
        command.humidity = 10;
        command.version = cycle.getVersion();
        //When
        CommandResult tested = handler.handle(command);
        //Then
        assertEquals(tested.entityId,cycle.getId());
        assertEquals(tested.status, CommandResult.Status.OK);
        assertEquals(tested.body,"CycleHumidityUpdated");
    }

    @Test
    public void shouldUpdateHumidity(){
        //Given
        Cycle cycle = createCycle();
        EditCycleHumidityCommand command = new EditCycleHumidityCommand();
        command.cycleId = cycle.getId();
        command.humidity = 46;
        command.version = cycle.getVersion();
        //When
        handler.handle(command);
        //Then
        assertEquals(46,cycle.getHumidity());
    }

    private Cycle createCycle() {
        return cycleFactory.getBuilder().initDate(20170202).chamber(createChamber.create()).technologist(userFactory.find(userService.getCurrentUserId())).build();
    }
}
