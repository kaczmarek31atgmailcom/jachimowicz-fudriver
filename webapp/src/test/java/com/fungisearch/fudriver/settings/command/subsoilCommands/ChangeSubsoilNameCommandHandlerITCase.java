package com.fungisearch.fudriver.settings.command.subsoilCommands;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.Subsoil;
import com.fungisearch.fudriver.settings.command.model.SubsoilFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.assertEquals;


@ContextConfiguration(locations = {"/test-spring.xml"})
public class ChangeSubsoilNameCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private SubsoilFactory subsoilFactory;

    @Autowired
    private ChangeSubsoilNameCommandHandler handler;

    @Test
    public void shouldChangeSubsoilName(){
        //Given
        Subsoil subsoil = subsoilFactory.getBuilder().name("test_subsoil").build();
        ChangeSubsoilNameCommand command = new ChangeSubsoilNameCommand();
        command.id = subsoil.getId();
        command.name = "ala_ma_kota";
        //When
        CommandResult commandResult = handler.handle(command);
        Subsoil tested = subsoilFactory.find(commandResult.entityId);
        //Then
        assertEquals(commandResult.status, CommandResult.Status.OK);
        assertEquals(commandResult.body,"SubsoilNameChanged");
        assertEquals(tested.getName(),"ala_ma_kota");
    }
}