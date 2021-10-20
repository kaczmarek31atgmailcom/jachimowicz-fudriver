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
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


@ContextConfiguration(locations = {"/test-spring.xml"})
public class EditCycleStartThirdPeriodDateCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    public CreateChamber createChamber;
    @Autowired
    public CycleFactory cycleFactory;
    @Autowired
    private EditCycleStartThirdPeriodDateCommandHandler handler;
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
        Cycle cycle = createCycle(20170101, 20170102);
        EditCycleStartThirdPeriodDateCommand command = new EditCycleStartThirdPeriodDateCommand();
        command.cycleId = cycle.getId();
        command.startThirdPeriod = 20170103;
        command.version = cycle.getVersion();
        //When
        CommandResult commandResult = handler.handle(command);
        //Then
        assertEquals(commandResult.entityId, cycle.getId());
        assertEquals(commandResult.status, CommandResult.Status.OK);
        assertEquals(commandResult.body, "CycleStartThirdPeriodDateUpdated");
    }

    @Test
    public void shouldNotCreateOpenThirdPeriodIfSecondIsNotOpened() {
        //Given
        Cycle cycle = createCycle(20170101);
        EditCycleStartThirdPeriodDateCommand command = new EditCycleStartThirdPeriodDateCommand();
        command.cycleId = cycle.getId();
        command.startThirdPeriod = 20170105;
        command.version = cycle.getVersion();
        //When & Then
        try {
            handler.handle(command);
            fail();
        } catch (IllegalStateException ex) {
        }
    }


    @Test
    public void shouldNotCreateOpenThirdPeriodBeforeStartSecondPeriodDate() {
        //Given
        Cycle cycle = createCycle(20170101, 20170120);
        EditCycleStartThirdPeriodDateCommand command = new EditCycleStartThirdPeriodDateCommand();
        command.cycleId = cycle.getId();
        command.startThirdPeriod = 20170105;
        command.version = cycle.getVersion();
        //When & Then
        try {
            handler.handle(command);
            fail();
        } catch (IllegalStateException ex) {
        }
    }

    @Test
    public void shouldStartThirdPeriod() {
        //Given
        Cycle cycle = createCycle(20170101, 20170120);
        EditCycleStartThirdPeriodDateCommand command = new EditCycleStartThirdPeriodDateCommand();
        command.cycleId = cycle.getId();
        command.startThirdPeriod = 20170125;
        command.version = cycle.getVersion();
        //When
        handler.handle(command);
        // Then
        assertEquals(cycle.getStartThirdPeriod(), new Integer(20170125));
    }


    private Cycle createCycle(int startDate) {
        Chamber chamber = createChamber.create();
        Cycle cycle = cycleFactory.getBuilder().chamber(chamber).technologist(userFactory.find(userService.getCurrentUserId())).initDate(startDate).build();
        return cycle;
    }

    private Cycle createCycle(int startDate, int startSecondPeriodDate) {
        Chamber chamber = createChamber.create();
        Cycle cycle = cycleFactory.getBuilder().chamber(chamber).technologist(userFactory.find(userService.getCurrentUserId())).initDate(startDate).build();
        cycle.edit(new Cycle.Edit().startSecondPeriod(startSecondPeriodDate));
        return cycle;
    }


}
