package com.fungisearch.fudriver.cycle.command;

import com.fungisearch.fudriver.common.CreateFakeAuthentication;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import com.fungisearch.fudriver.settings.command.model.Subsoil;
import com.fungisearch.fudriver.settings.command.model.SubsoilFactory;
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
public class EditCycleSubsoilCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CycleFactory cycleFactory;
    @Autowired
    private CreateChamber createChamber;
    @Autowired
    private UserService userService;
    @Autowired
    private UserFactory userFactory;
    @Autowired
    private EditCycleSubsoilCommandHandler handler;
    @Autowired
    private SubsoilFactory subsoilFactory;

    @Before
    public void setUp() {
        CreateFakeAuthentication.authenticatate();
    }

    @Test
    public void shouldReturnValidCommandResult() {
        //Given
        Cycle cycle = createCycle();
        Subsoil subsoil = subsoilFactory.getBuilder().name("test").build();
        EditCycleSubsoilCommand command = new EditCycleSubsoilCommand();
        command.cycleId = cycle.getId();
        command.subsoilId = subsoil.getId();
        command.version = cycle.getVersion();
        //When
        CommandResult tested = handler.handle(command);
        //Then
        assertEquals(cycle.getId(), tested.entityId);
        assertEquals(CommandResult.Status.OK, tested.status);
        assertEquals("CycleSubsoilUpdated", tested.body);
    }


    @Test
    public void shouldUpdateSubsoil() {
        //Given
        Cycle cycle = createCycle();
        Subsoil subsoil = subsoilFactory.getBuilder().name("test").build();
        EditCycleSubsoilCommand command = new EditCycleSubsoilCommand();
        command.cycleId = cycle.getId();
        command.subsoilId = subsoil.getId();
        command.version = cycle.getVersion();
        //When
        handler.handle(command);
        //Then
        assertEquals(subsoil, cycle.getSubsoil());
    }

    @Test
    public void shouldNotUpdateOnInvalidVersion() {
        //Given
        Cycle cycle = createCycle();
        Subsoil subsoil = subsoilFactory.getBuilder().name("test").build();
        EditCycleSubsoilCommand command = new EditCycleSubsoilCommand();
        command.cycleId = cycle.getId();
        command.subsoilId = subsoil.getId();
        command.version = cycle.getVersion() + 1;
        //When & Then
        try {
            handler.handle(command);
            fail();
        } catch (StaleObjectStateException ex){}
    }


    private Cycle createCycle() {
        return cycleFactory.getBuilder().chamber(createChamber.create()).initDate(20170101).technologist(userFactory.find(userService.getCurrentUserId())).build();
    }

}
