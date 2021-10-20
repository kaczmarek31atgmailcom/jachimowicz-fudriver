package com.fungisearch.fudriver.type.command.model;

import com.fungisearch.fudriver.testTools.CreateType;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = {"/test-spring.xml"})
public class TypeFactoryITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private TypeFactory typeFactory;

    @Autowired
    private CreateType createType;

    @Test
    public void shouldReturnType(){
        //Given
        Type type = createType.create();
        Assert.assertEquals(type.getExportType(),ExportType.EXPORT);
        //When
        Type tested = typeFactory.findById(type.getId());
        Assert.assertEquals(tested.getExportType(),ExportType.EXPORT);
    }


}