package com.fungisearch.fudriver.type.command;

import com.fungisearch.fudriver.box.command.model.Box;
import com.fungisearch.fudriver.box.command.model.BoxFactory;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.type.command.model.ExportType;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.type.command.model.TypeGroup;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.*;

@ContextConfiguration(locations = "/test-spring.xml")
public class DeleteTypeCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests{

    @Autowired
    private DeleteTypeCommandHandler handler;
    @Autowired
    private TypeFactory typeFactory;
    @Autowired
    private BoxFactory boxFactory;

    @Test
    public void shouldRemoveType(){
        //give
        TypeGroup typeGroup = typeFactory.getTypeGroupBuilder().name("typeGroup").build();
        Box box = boxFactory.getBuilder().name("box").build();
        Type tested =  typeFactory.getBuilder()
                .typeGroup(typeGroup)
                .box(box)
                .name("type_name")
                .exportType(ExportType.EXPORT)
                .weight(1000)
                .build();
        assertFalse(tested.getArchived());
        //When
        CommandResult commandResult = handler.handle(tested.getId());
        //Then
        assertTrue(tested.getArchived());
        assertEquals(commandResult.entityId,tested.getId());
        assertEquals(commandResult.status, CommandResult.Status.OK);
        assertEquals(commandResult.body,"TypeDeleted");
    }

}