package com.fungisearch.fudriver.type.command;

import com.fungisearch.fudriver.box.command.model.Box;
import com.fungisearch.fudriver.box.command.model.BoxFactory;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.type.command.model.ExportType;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.type.command.model.TypeGroup;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.assertEquals;

//@ContextConfiguration(locations = {"/test-spring.xml"})
//public class CreateTypeCommandHandlerTest extends AbstractTransactionalJUnit4SpringContextTests {
public class CreateTypeCommandHandlerTest{
    @Autowired
    private TypeFactory typeFactory;
    @Autowired
    private BoxFactory boxFactory;
    @Autowired
    private CreateTypeCommandHandler handler;

    private TypeGroup typeGroup;
    private Box box;
    private CreateTypeCommand command;

    //@Before
    public void setUp() {
        typeGroup = typeFactory.getTypeGroupBuilder().name("type group").build();
        box = boxFactory.getBuilder().name("box").build();
        command = new CreateTypeCommand();
        command.exportType = "EXPORT";
        command.boxId = box.getId();
        command.groupId = typeGroup.getId();
        command.name = "type";
    }

    
    public void shouldCreateType() {
        //Given
        command.weight = 1000L;
        //When
        CommandResult commandResult = handler.handle(command);
        //Then
        Type tested = typeFactory.findById(commandResult.entityId);
        assertEquals("type",tested.getName());
        assertEquals(new Double(1.00),tested.getWeight());
        assertEquals(ExportType.EXPORT,tested.getExportType());
        assertEquals(box,tested.getBox());
        assertEquals(typeGroup, tested.getTypeGroup());
    }


    public void shouldCreate049Weight(){
        //Given
        command.weight = 49L;
        //When
        CommandResult commandResult = handler.handle(command);
        Type tested = typeFactory.findById(commandResult.entityId);
        //Then
        assertEquals(new Double(0.049),tested.getWeight());
    }
}
