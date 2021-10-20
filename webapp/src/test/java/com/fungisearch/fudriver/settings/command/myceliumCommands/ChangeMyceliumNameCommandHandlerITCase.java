package com.fungisearch.fudriver.settings.command.myceliumCommands;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.Mycelium;
import com.fungisearch.fudriver.settings.command.model.MyceliumFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(locations = {"/test-spring.xml"})
public class ChangeMyceliumNameCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private MyceliumFactory myceliumFactory;
    @Autowired
    private ChangeMyceliumNameCommandHandler handler;

    @Test
    public void shouldProduceValidCommandResult(){
        //Given
        Mycelium mycelium = myceliumFactory.getBuilder().name("old_name").build();
        ChangeMyceliumNameCommand command = new ChangeMyceliumNameCommand();
        command.id = mycelium.getId();
        command.name = "new_name";
        //When
        CommandResult commandResult = handler.handle(command);
        //Then
        assertEquals(commandResult.entityId,mycelium.getId());
        assertEquals(commandResult.status, CommandResult.Status.OK);
        assertEquals(commandResult.body,"MyceliumNameChanged");
    }

    @Test
    public void shouldChangeMyceliumName(){
        //Given
        Mycelium mycelium = myceliumFactory.getBuilder().name("old_name").build();
        ChangeMyceliumNameCommand command = new ChangeMyceliumNameCommand();
        command.id = mycelium.getId();
        command.name = "new_name";
        //When
        handler.handle(command);
        //Then
        assertEquals(mycelium.getName(),"new_name");
    }
}