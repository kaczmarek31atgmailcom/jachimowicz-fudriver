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
public class EditCycleAreaCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private UserService userService;
    @Autowired
    private UserFactory userFactory;
    @Autowired
    private CreateChamber createChamber;
    @Autowired
    private CycleFactory cycleFactory;
    @Autowired
    private EditCycleAreaCommandHandler handler;


    @Before
    public void setUp() {
        CreateFakeAuthentication.authenticatate();
    }

    @Test
    public void shouldCreateValidCommanResult() {
        //Given
        Cycle cycle = createCycle();
        EditCycleAreaCommand command = new EditCycleAreaCommand();
        command.cycleId = cycle.getId();
        command.area = 1450;
        command.version = cycle.getVersion();
        //When
        CommandResult tested = handler.handle(command);
        //Then
        assertEquals(tested.entityId, cycle.getId());
        assertEquals(tested.status, CommandResult.Status.OK);
        assertEquals(tested.body, "CycleAreaUpdated");
    }

    @Test
    public void shouldUpdateCycleArea() {
        //Given
        Cycle cycle = createCycle();
        EditCycleAreaCommand command = new EditCycleAreaCommand();
        command.cycleId = cycle.getId();
        command.area = 1242;
        command.version = cycle.getVersion();
        //When
        handler.handle(command);
        //Then
        assertEquals(cycle.getArea(), 1242);
    }

    @Test
    public void shouldNotUpdateCycleAreaWhenVersionNotMathes() {
        //Given
        Cycle cycle = createCycle();
        EditCycleAreaCommand command = new EditCycleAreaCommand();
        command.cycleId = cycle.getId();
        command.area = 1234;
        command.version = cycle.getVersion() + 1;
        //When & Then
        try {
            handler.handle(command);
            fail();
        } catch (StaleObjectStateException ex) {
        }
    }

    @Test
    public void CycleAreaShouldBeGraterThenZero() {
        //Given
        Cycle cycle = createCycle();
        EditCycleAreaCommand command = new EditCycleAreaCommand();
        command.cycleId = cycle.getId();
        command.area = 0;
        command.version = cycle.getVersion();
        //When & Then
        try {
            handler.handle(command);
            fail();
        } catch (IllegalStateException ex) {
        }
    }

    private Cycle createCycle() {
        Chamber chamber = createChamber.create();
        Cycle cycle = cycleFactory.getBuilder().chamber(chamber).technologist(userFactory.find(userService.getCurrentUserId())).initDate(20170101).build();
        return cycle;
    }


}
