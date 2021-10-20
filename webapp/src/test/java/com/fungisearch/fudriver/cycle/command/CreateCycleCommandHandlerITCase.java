package com.fungisearch.fudriver.cycle.command;

import com.fungisearch.fudriver.common.CreateFakeAuthentication;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import com.fungisearch.fudriver.settings.command.model.Chamber;
import com.fungisearch.fudriver.testTools.UT.CreateChamber;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(locations = {"/test-spring.xml"})
public class CreateCycleCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests{

    @Autowired
    private CreateChamber createChamber;

    @Autowired
    private CreateCycleCommandHandler handler;
    @Autowired
    private CycleFactory cycleFactory;


    @Before
    public void setUp(){
        new CreateFakeAuthentication().authenticatate();
    }

    @Test
    public void shouldReturnCommandResultOnCycleCreation(){
        //Given
        Chamber chamber = createChamber.create();
        CreateCycleCommand command = new CreateCycleCommand();
        command.chamberId = chamber.getId();
        command.startDate = 201701;
        //When
        CommandResult commandResult = handler.handle(command);
        //Then
        Assert.assertNotNull(commandResult.entityId);
        assertEquals(commandResult.status, CommandResult.Status.OK);
        assertEquals(commandResult.body,"CycleCreated");
    }

    @Test
    public void shouldCreateCycleForValidChamber(){
        //Given
        Chamber chamber = createChamber.create();
        CreateCycleCommand command = new CreateCycleCommand();
        command.chamberId = chamber.getId();
        command.startDate = 201701;
        //When
        CommandResult commandResult = handler.handle(command);
        Cycle tested = cycleFactory.find(commandResult.entityId);
        //Then
        assertEquals(tested.getChamber(),chamber);
    }

    @Test
    public void shouldCreateCycleWithValidStartDate(){
        //Given
        Chamber chamber = createChamber.create();
        CreateCycleCommand command = new CreateCycleCommand();
        command.chamberId = chamber.getId();
        command.startDate = 20170101;
        //When
        CommandResult commandResult = handler.handle(command);
        Cycle tested = cycleFactory.find(commandResult.entityId);
        //Then
        assertEquals(tested.getInitDate(),new Integer(20170101));
    }

    @Test
    public void shouldCreateCycleWithAreaEqualDefaultChamberArea(){
        //Given
        Chamber chamber = createChamber.create();
        CreateCycleCommand command = new CreateCycleCommand();
        command.chamberId = chamber.getId();
        command.startDate = 20170101;
        //When
        CommandResult commandResult = handler.handle(command);
        Cycle tested = cycleFactory.find(commandResult.entityId);
        //Then
        assertEquals(tested.getArea(),chamber.getArea());
    }

    @Test
    public void shouldUpdateStartDateOfExistingOpenCycle(){
        //Given
        Chamber chamber = createChamber.create();
        CreateCycleCommand command = new CreateCycleCommand();
        command.chamberId = chamber.getId();
        command.startDate = 20170101;
        CreateCycleCommand command2 = new CreateCycleCommand();
        command2.chamberId = chamber.getId();
        command2.startDate = 20170102;
        //When
        handler.handle(command);
        CommandResult commandResult = handler.handle(command2);
        Cycle tested = cycleFactory.find(commandResult.entityId);
        //Then
        assertEquals(tested.getInitDate(),new Integer(20170102));
    }

}
