package com.fungisearch.fudriver.type.command;

import com.fungisearch.fudriver.box.command.model.Box;
import com.fungisearch.fudriver.box.command.model.BoxFactory;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.payroll.wage.command.model.WageFactory;
import com.fungisearch.fudriver.payroll.wage.command.model.WageHeader;
import com.fungisearch.fudriver.type.command.model.ExportType;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.type.command.model.TypeGroup;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.*;

@ContextConfiguration(locations = {"/test-spring.xml"})
public class CreateTypeCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests{

    @Autowired
    private  CreateTypeCommandHandler handler;
    @Autowired
    private TypeFactory typeFactory;
    @Autowired
    private WageFactory wageFactory;
    @Autowired
    private BoxFactory boxFactory;


    @Test
    public void shouldCreateTypeWithoutBoxAndGroup(){
        //Given
        CreateTypeCommand command = new CreateTypeCommand();
        command.exportType = "EXPORT";
        command.name = "test_type";
        command.weight = 100;
        //When
        CommandResult commandResult = handler.handle(command);
        //Then
        assertNotNull(commandResult.entityId);
        Type tested = typeFactory.findById(commandResult.entityId);
        assertEquals(tested.getWeight(),new Double(0.1));
        assertEquals(tested.getExportType(), ExportType.EXPORT);
        assertEquals(tested.getName(),"test_type");
        assertFalse(tested.getArchived());
    }

    @Test
    public void shouldCreateWagesForNewType(){
        //Given
        WageHeader wageHeader = wageFactory.headerBuilder().name("wage_header").build();
        CreateTypeCommand command = new CreateTypeCommand();
        command.exportType = "EXPORT";
        command.name = "test_type";
        command.weight = 100;
        //When
        CommandResult commandResult = handler.handle(command);
        //Then
        Type type = typeFactory.findById(commandResult.entityId);
        assertEquals(3,wageHeader.getWages().size());
        assertEquals(wageHeader.getWages().get(0).getType(),type);
        assertEquals(wageHeader.getWages().get(1).getType(),type);
        assertEquals(wageHeader.getWages().get(2).getType(),type);
    }

    @Test
    public void shouldCreateTypeWithGivenTypeGroup(){
        //Given
        TypeGroup typeGroup = typeFactory.getTypeGroupBuilder().name("type_group").build();
        CreateTypeCommand command = new CreateTypeCommand();
        command.exportType = "EXPORT";
        command.name = "test_type";
        command.weight = 100;
        command.groupId = typeGroup.getId();
        //When
        CommandResult commandResult = handler.handle(command);
        //Then
        Type type = typeFactory.findById(commandResult.entityId);
        assertEquals(typeGroup,type.getTypeGroup());
    }

    @Test
    public void shouldCreateTypeWithGivenBox(){
        //Given
        Box box = boxFactory.getBuilder().name("box").build();
        CreateTypeCommand command = new CreateTypeCommand();
        command.exportType = "EXPORT";
        command.name = "test_type";
        command.weight = 100;
        command.boxId = box.getId();
        //When
        CommandResult commandResult = handler.handle(command);
        //Then
        Type type = typeFactory.findById(commandResult.entityId);
        assertEquals(box,type.getBox());
    }
}