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

import static java.lang.Math.toIntExact;
import static org.junit.Assert.assertEquals;


@ContextConfiguration(locations = {"/test-spring.xml"})
public class EditCycleInitDateCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CycleFactory cycleFactory;
    @Autowired
    private CreateChamber createChamber;
    @Autowired
    private UserFactory userFactory;
    @Autowired
    private UserService userService;
    @Autowired
    private EditCycleStartDateCommandHandler handler;

    @Before
    public void setUp(){
        CreateFakeAuthentication.authenticatate();
    }

    @Test
    public void shouldReturnValidCommandResult(){
        //Before
        Cycle cycle =cycleFactory.getBuilder().initDate(20170101).chamber(createChamber.create()).technologist(userFactory.find(userService.getCurrentUserId())).build();
        EditCycleStartDateCommand command = new EditCycleStartDateCommand();
        command.cycleId = toIntExact(cycle.getId());
        command.version = cycle.getVersion();
        command.startDate = 20170202;
        //When
        CommandResult commandResult = handler.handle(command);
        //Then
        assertEquals(commandResult.entityId,cycle.getId());
        assertEquals(commandResult.status, CommandResult.Status.OK);
        assertEquals(commandResult.body,"CycleUpdated");
    }

    @Test
    public void shouldUpdateCycleStartDate(){
        //Before
        Cycle cycle =cycleFactory.getBuilder().initDate(20170101).chamber(createChamber.create()).technologist(userFactory.find(userService.getCurrentUserId())).build();
        EditCycleStartDateCommand command = new EditCycleStartDateCommand();
        command.cycleId = toIntExact(cycle.getId());
        command.version = cycle.getVersion();
        command.startDate = 20170202;
        //When
        handler.handle(command);
        //Then
        assertEquals(cycle.getInitDate(),new Integer(20170202));
    }


}
