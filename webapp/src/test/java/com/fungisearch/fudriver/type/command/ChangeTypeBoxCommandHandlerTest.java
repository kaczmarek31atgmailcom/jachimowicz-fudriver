package com.fungisearch.fudriver.type.command;

import com.fungisearch.fudriver.box.command.model.Box;
import com.fungisearch.fudriver.box.command.model.BoxFactory;
import com.fungisearch.fudriver.type.command.model.ExportType;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.*;

//@ContextConfiguration(locations={"/test-spring.xml"})
//public class ChangeTypeBoxCommandHandlerTest extends AbstractTransactionalJUnit4SpringContextTests {
public class ChangeTypeBoxCommandHandlerTest {

    @Autowired
    private ChangeTypeBoxCommandHandler handler;

    @Autowired
    private BoxFactory boxFactory;
    @Autowired
    private TypeFactory typeFactory;


    public void shouldChangeBox(){
        //Given
        Box box1 = boxFactory.getBuilder().name("box1").build();
        Box box2 = boxFactory.getBuilder().name("box2").build();
        Type type = typeFactory.getBuilder().box(box1).name("type").exportType(ExportType.EXPORT).weight(1).build();
        ChangeTypeBoxCommand command = new ChangeTypeBoxCommand();
        command.typeId = type.getId();
        command.boxId = box2.getId();
        //When
        handler.handle(command);
        //Then
        Assert.assertEquals(type.getBox(),box2);
    }

}
