package com.fungisearch.fudriver.settings.command.myceliumCommands;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.Mycelium;
import com.fungisearch.fudriver.settings.command.model.MyceliumFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@ContextConfiguration(locations = {"/test-spring.xml"})
public class DeleteMyceliumCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private MyceliumFactory myceliumFactory;
    @Autowired
    private DeleteMyceliumCommandHandler handler;

    @Test
    public void shouldProduceValidCommandResult(){
        //Given
        Mycelium mycelium = myceliumFactory.getBuilder().name("mycelium").build();
        //When
        CommandResult commandResult = handler.handle(mycelium.getId());
        //Then
        assertEquals(commandResult.entityId,mycelium.getId());
        assertEquals(commandResult.status, CommandResult.Status.OK);
        assertEquals(commandResult.body,"MyceliumDeleted");
    }

    @Test
    public void shouldDeleteMycelium(){
        //Given
        Mycelium mycelium = myceliumFactory.getBuilder().name("myceliumm").build();
        assertTrue(mycelium.isActive());
        //When
        handler.handle(mycelium.getId());
        //Then
        assertFalse(mycelium.isActive());
    }
}