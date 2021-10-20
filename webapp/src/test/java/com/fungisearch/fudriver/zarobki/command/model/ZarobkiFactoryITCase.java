package com.fungisearch.fudriver.zarobki.command.model;

import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.testTools.CreateCycle;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;


@ContextConfiguration(locations = {"/test-spring.xml"})
public class ZarobkiFactoryITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CreateCycle createCycle;
    @Autowired
    ZarobkiFactory zarobkiFactory;

    @Test
    public void shouldReturnNullAsMinDateForCycleWithoutHarvest(){
        //Given
        Cycle cycle = createCycle.create();
        //When
        Integer tested = zarobkiFactory.findMinDateForCycle(cycle.getId());
        //Then
        Assert.assertNull(tested);
    }

}