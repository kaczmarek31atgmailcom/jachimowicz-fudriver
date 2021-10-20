package com.fungisearch.fudriver.payroll.wage.command;

import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.wage.command.model.Wage;
import com.fungisearch.fudriver.payroll.wage.command.model.WageFactory;
import com.fungisearch.fudriver.payroll.wage.command.model.WageHeader;
import com.fungisearch.fudriver.testTools.CreateType;
import com.fungisearch.fudriver.testTools.FlushDao;
import com.fungisearch.fudriver.type.command.model.Type;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = {"/test-spring.xml"})
public class UpdateWageValueCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CreateType createType;
    @Autowired
    private WageFactory wageFactory;
    @Autowired
    private UpdateWageValueCommandHandler updateWageValueCommandHandler;
    @Autowired
    private FlushDao flushDao;

    @Test
    public void shouldUpdateWageValue() {
        //Given
        Type type = createType.create();
        WageHeader wageHeader = wageFactory.headerBuilder().name("test_header").build();
        Wage wage = wageFactory.builder().wageHeader(wageHeader).type(type).dayType(SalaryDayTypeEnum.REGULAR_DAY).value(1L).build();
        flushDao.flush();
        UpdateWageValueCommand command = new UpdateWageValueCommand();
        command.wageId = wage.getId();
        command.value = 2L;
        //When
        updateWageValueCommandHandler.handle(command);
        //Then
        Assert.assertEquals(new Long(2),wage.getValue());
    }
}