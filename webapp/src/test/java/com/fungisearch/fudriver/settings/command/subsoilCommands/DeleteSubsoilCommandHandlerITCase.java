package com.fungisearch.fudriver.settings.command.subsoilCommands;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.Subsoil;
import com.fungisearch.fudriver.settings.command.model.SubsoilFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


@ContextConfiguration(locations = {"/test-spring.xml"})
public class DeleteSubsoilCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private SubsoilFactory subsoilFactory;

    @Autowired
    private DeleteSubsoilCommandHandler handler;

    @Test
    public void shouldDeleteSubsoil(){
        //Given
        Subsoil subsoil = subsoilFactory.getBuilder().name("test_subsoil").build();
        //When
        handler.handle(subsoil.getId());
        //Then
        assertFalse(subsoil.isActive());
    }

    @Test
    public void shouldCreateValidCommandResultWhenDeletingSubsoil(){
        //Given
        Subsoil subsoil = subsoilFactory.getBuilder().name("test_subsoil").build();
        //When
        CommandResult commandResult = handler.handle(subsoil.getId());
        //Then
        assertEquals(commandResult.entityId,subsoil.getId());
        assertEquals(commandResult.status, CommandResult.Status.OK);
        assertEquals(commandResult.body,"SubsoilDeleted");
    }
}