package com.fungisearch.fudriver.cycle.command;

import com.fungisearch.fudriver.common.CreateFakeAuthentication;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import com.fungisearch.fudriver.settings.command.model.Chamber;
import com.fungisearch.fudriver.testTools.FlushDao;
import com.fungisearch.fudriver.testTools.UT.CreateChamber;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import com.fungisearch.fudriver.user.query.service.UserService;
import org.hibernate.StaleObjectStateException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.fail;

@ContextConfiguration(locations = {"/test-spring.xml"})
public class EditCycleStartSecondPeriodCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    public CreateChamber createChamber;
    @Autowired
    public CycleFactory cycleFactory;
    @Autowired
    private EditCycleStartSecondPeriodCommandHandler handler;
    @Autowired
    private UserService userService;
    @Autowired
    private UserFactory userFactory;
    @Autowired
    private FlushDao flushDao;

    @Before
    public void setUp() {
        CreateFakeAuthentication.authenticatate();
    }

    @Test
    public void shouldCreateValidCommandResult() {
        //Given
        Cycle cycle = createCycle();
        EditCycleStartSecondPeriodCommand command = new EditCycleStartSecondPeriodCommand();
        command.cycleId = cycle.getId();
        command.startSecondPeriod = 20170102;
        command.version = cycle.getVersion();
        //When
        CommandResult commandResult = handler.handle(command);
        //Then
        Assert.assertEquals(commandResult.entityId, cycle.getId());
        Assert.assertEquals(commandResult.status, CommandResult.Status.OK);
        Assert.assertEquals(commandResult.body, "StartSecondPeriodDateUpdated");
    }

    @Test
    public void shouldSetStartSecondPeriodWhenItIsNull() {
        //Given
        Cycle cycle = createCycle();
        EditCycleStartSecondPeriodCommand command = new EditCycleStartSecondPeriodCommand();
        command.cycleId = cycle.getId();
        command.startSecondPeriod = 20170102;
        command.version = cycle.getVersion();
        //When
        handler.handle(command);
        //Then
        Assert.assertEquals(cycle.getStartSecondPeriod(), new Integer(20170102));
    }

    @Test
    public void shouldSetStartSecondPeriodWhenItIsNotNull() {
        //Given
        Cycle cycle = createCycle();
        EditCycleStartSecondPeriodCommand command = new EditCycleStartSecondPeriodCommand();
        command.cycleId = cycle.getId();
        command.startSecondPeriod = 20170102;
        command.version = cycle.getVersion();
        handler.handle(command);
        EditCycleStartSecondPeriodCommand command2 = new EditCycleStartSecondPeriodCommand();
        command2.cycleId = cycle.getId();
        command2.startSecondPeriod = 20170103;
        command2.version = cycle.getVersion();
        //When
        handler.handle(command2);
        //Then
        Assert.assertEquals(cycle.getStartSecondPeriod(), new Integer(20170103));
    }

    @Test
    public void shouldNotSetStartSecondPeriodBeforeStartDate() {
        //Given
        Cycle cycle = createCycle();
        EditCycleStartSecondPeriodCommand command = new EditCycleStartSecondPeriodCommand();
        command.cycleId = cycle.getId();
        command.startSecondPeriod = 20161231;
        command.version = cycle.getVersion();
        //When & Then
        try {
            handler.handle(command);
            fail();
        } catch (IllegalStateException ex) {
        }
    }

    @Test
    public void shouldNotChangeDateIfVersionNotMatch() {
        //Given
        Cycle cycle = createCycle();
        EditCycleStartSecondPeriodCommand command = new EditCycleStartSecondPeriodCommand();
        command.cycleId = cycle.getId();
        command.startSecondPeriod = 20170102;
        command.version = cycle.getVersion();
        handler.handle(command);
        flushDao.flush();
        command.startSecondPeriod = 20170203;
        //When & Then
        try {
            handler.handle(command);
            fail();
        } catch (StaleObjectStateException ex) {
        }
        Assert.assertEquals(cycle.getStartSecondPeriod(), new Integer(20170102));
    }


    private Cycle createCycle() {
        Chamber chamber = createChamber.create();
        Cycle cycle = cycleFactory.getBuilder().chamber(chamber).technologist(userFactory.find(userService.getCurrentUserId())).initDate(20170101).build();
        return cycle;
    }

}
