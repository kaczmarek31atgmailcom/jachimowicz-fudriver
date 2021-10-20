package com.fungisearch.fudriver.settings.command.myceliumCommands;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.Mycelium;
import com.fungisearch.fudriver.settings.command.model.MyceliumFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@ContextConfiguration(locations = {"/test-spring.xml"})
public class CreateMyceliumCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private MyceliumFactory myceliumFactory;
    @Autowired
    CreateMyceliumCommandHandler handler;

    @Test
    public void shouldCreateValidCommandResult(){
        //Given
        CreateMyceliumCommand command = new CreateMyceliumCommand();
        command.name = "A15";
        //When
        CommandResult tested = handler.handle(command);
        //Then
        assertNotNull(tested.entityId);
        assertEquals(tested.status, CommandResult.Status.OK);
        assertEquals(tested.body, "MyceliumCreated");
    }

    @Test
    public void shouldCreateMycelium(){
        //Given
        CreateMyceliumCommand command = new CreateMyceliumCommand();
        command.name = "ala";
        //When
        CommandResult commandResult = handler.handle(command);
        Mycelium mycelium = myceliumFactory.find(commandResult.entityId);
        //Then
        assertNotNull(mycelium.getId());
        assertEquals(mycelium.getId(),commandResult.entityId);
        assertEquals(mycelium.getName(),"ala");
        assertTrue(mycelium.isActive());
    }
}