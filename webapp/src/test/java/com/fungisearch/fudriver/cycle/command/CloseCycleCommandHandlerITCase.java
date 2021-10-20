package com.fungisearch.fudriver.cycle.command;

import com.fungisearch.fudriver.common.CreateFakeAuthentication;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import com.fungisearch.fudriver.cycle.command.model.CycleStatusEnum;
import com.fungisearch.fudriver.testTools.CreateCycle;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import com.fungisearch.fudriver.user.query.service.UserService;
import org.hibernate.StaleObjectStateException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;


import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;


@ContextConfiguration(locations = {"/test-spring.xml"})
public class CloseCycleCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private CycleFactory cycleFactory;
    @Autowired
    private UserService userService;
    @Autowired
    private UserFactory userFactory;
    @Autowired
    private CreateCycle createCycle;
    @Autowired
    private CloseCycleCommandHandler handler;

    @Before
    public void setUp(){
        CreateFakeAuthentication.authenticatate();
    }

    @Test
    public void shouldCreateValidCommandResult(){
        //Given
        Cycle cycle = createCycle.create(20170101,20170102,20170103);
        CloseCycleCommand command = new CloseCycleCommand();
        command.cycleId = cycle.getId();
        command.version = cycle.getVersion();
        command.closeDate = 20170104;
        //When
        CommandResult tested = handler.handle(command);
        //Then
        assertEquals(cycle.getId(),tested.entityId);
        assertEquals(CommandResult.Status.OK,tested.status);
        assertEquals("CycleClosed",tested.body);
    }

    @Test
    public void shouldNotCloseWhenVersionNotMatch(){
        Cycle cycle = createCycle.create(20170101,20170102,20170103);
        CloseCycleCommand command = new CloseCycleCommand();
        command.cycleId = cycle.getId();
        command.version = cycle.getVersion() + 1;
        command.closeDate = 20170104;
        //When & Then
        try {
            handler.handle(command);
            fail();
        } catch (StaleObjectStateException ex) {}
    }

    @Test
    public void shouldCloseCycle(){
        Cycle cycle = createCycle.create(20170101,20170102,20170103);
        CloseCycleCommand command = new CloseCycleCommand();
        command.cycleId = cycle.getId();
        command.version = cycle.getVersion();
        command.closeDate = 20170104;
        //When
        handler.handle(command);
        //Then
        assertEquals(CycleStatusEnum.CLOSED,cycle.getCycleStatus());
        assertEquals(new Integer(20170104),cycle.getEnd());
    }
}
