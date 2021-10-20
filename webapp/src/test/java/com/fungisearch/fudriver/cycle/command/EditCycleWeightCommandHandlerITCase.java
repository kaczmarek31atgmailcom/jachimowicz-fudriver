package com.fungisearch.fudriver.cycle.command;

import com.fungisearch.fudriver.common.CreateFakeAuthentication;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import com.fungisearch.fudriver.settings.command.model.Chamber;
import com.fungisearch.fudriver.testTools.UT.CreateChamber;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import com.fungisearch.fudriver.user.query.service.UserService;
import org.hibernate.StaleObjectStateException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@ContextConfiguration(locations = {"/test-spring.xml"})
public class EditCycleWeightCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private UserService userService;
    @Autowired
    private UserFactory userFactory;
    @Autowired
    private CreateChamber createChamber;
    @Autowired
    private EditCycleWeightCommandHandler handler;
    @Autowired
    private CycleFactory cycleFactory;

    @Before
    public void setUp(){
        CreateFakeAuthentication.authenticatate();
    }

    @Test
    public void shouldCreateValidCommandResult(){
        //Given
        Cycle cycle = createCycle();
        EditCycleWeightCommand command = new EditCycleWeightCommand();
        command.cycleId = cycle.getId();
        command.weight = 123;
        command.version = cycle.getVersion();
        //When
        CommandResult commandResult = handler.handle(command);
        //Then
        assertEquals(commandResult.entityId,cycle.getId());
        assertEquals(commandResult.status, CommandResult.Status.OK);
        assertEquals(commandResult.body,"CycleWeightUpdated");
    }

    @Test
    public void weightShouldNotBeBelowOrEqualZero(){
        //Given
        Cycle cycle = createCycle();
        EditCycleWeightCommand command = new EditCycleWeightCommand();
        command.cycleId = cycle.getId();
        command.weight = 0;
        command.version = cycle.getVersion();
        //When & Then
        try{
            handler.handle(command);
            fail();
        } catch(IllegalStateException ex){}
    }

    @Test
    public void shouldUpdateWeight(){
        //Given
        Cycle cycle = createCycle();
        int weight = cycle.getWeight() + 100;
        EditCycleWeightCommand command = new EditCycleWeightCommand();
        command.cycleId = cycle.getId();
        command.weight = weight;
        command.version = cycle.getVersion();
        //When
        handler.handle(command);
        //Then
        assertEquals(cycle.getWeight(),weight);
    }

    @Test
    public void shouldFailOnNotInvalidVersion(){
        //Given
        Cycle cycle = createCycle();
        int weight = cycle.getWeight() + 100;
        EditCycleWeightCommand command = new EditCycleWeightCommand();
        command.cycleId = cycle.getId();
        command.weight = weight;
        command.version = cycle.getVersion() + 1;
        //When & Then
        try {
            handler.handle(command);
            fail();
        } catch (StaleObjectStateException ex){}
    }

    private Cycle createCycle() {
        Chamber chamber = createChamber.create();
        Cycle cycle = cycleFactory.getBuilder().chamber(chamber).technologist(userFactory.find(userService.getCurrentUserId())).initDate(20170101).build();
        return cycle;
    }

}
