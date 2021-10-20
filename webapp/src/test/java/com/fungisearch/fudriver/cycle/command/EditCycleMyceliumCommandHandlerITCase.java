package com.fungisearch.fudriver.cycle.command;

import com.fungisearch.fudriver.common.CreateFakeAuthentication;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import com.fungisearch.fudriver.settings.command.model.Mycelium;
import com.fungisearch.fudriver.settings.command.model.MyceliumFactory;
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
public class EditCycleMyceliumCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests{

    @Autowired
    private EditCycleMyceliumCommandHandler handler;
    @Autowired
    private UserService userService;
    @Autowired
    private UserFactory userFactory;
    @Autowired
    private CreateChamber createChamber;
    @Autowired
    private CycleFactory cycleFactory;
    @Autowired
    private MyceliumFactory myceliumFactory;


    @Before
    public void setUp() {
        CreateFakeAuthentication.authenticatate();
    }

    @Test
    public void shouldReturnValidCommandResult() {
        //Given
        Cycle cycle = createCycle();
        Mycelium mycelium = myceliumFactory.getBuilder().name("test").build();
        EditCycleMyceliumCommand command = new EditCycleMyceliumCommand();
        command.cycleId = cycle.getId();
        command.myceliumId = mycelium.getId();
        command.version = cycle.getVersion();
        //When
        CommandResult tested = handler.handle(command);
        //Then
        assertEquals(cycle.getId(), tested.entityId);
        assertEquals(CommandResult.Status.OK, tested.status);
        assertEquals("CycleMyceliumUpdated", tested.body);
    }

    @Test
    public void shouldUpdateCycleMycelium(){
        //Given
        Cycle cycle = createCycle();
        Mycelium mycelium = myceliumFactory.getBuilder().name("test").build();
        EditCycleMyceliumCommand command = new EditCycleMyceliumCommand();
        command.cycleId = cycle.getId();
        command.myceliumId = mycelium.getId();
        command.version = cycle.getVersion();
        //When
        handler.handle(command);
        //Then
        assertEquals(mycelium,cycle.getMycelium());
    }

    private Cycle createCycle() {
        return cycleFactory
                .getBuilder()
                .chamber(createChamber.create())
                .initDate(20170101)
                .technologist(userFactory.find(userService.getCurrentUserId()))
                .build();
    }

}
