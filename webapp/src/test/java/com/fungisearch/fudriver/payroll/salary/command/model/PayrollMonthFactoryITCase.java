package com.fungisearch.fudriver.payroll.salary.command.model;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.Date;

@ContextConfiguration(locations = {"/test-spring.xml"})
public class PayrollMonthFactoryITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private PayrollMonthFactory payrollMonthFactory;

    @Test
    public void shouldFindMonthByDay01(){
        //Given
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime dt = formatter.parseDateTime("2017-02-01");
        Date firstDayOfMonth = dt.withDayOfMonth(1).toDate();
        Date day = dt.toDate();
        //When
        PayrollMonth tested = payrollMonthFactory.findByDay(day);
        //Then
        Assert.assertEquals(firstDayOfMonth,tested.firstDay);
    }


    @Test
    public void shouldFindMonthByDay02(){
        //Given
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime dt = formatter.parseDateTime("2017-02-02");
        Date firstDayOfMonth = dt.withDayOfMonth(1).toDate();
        Date day = dt.toDate();
        //When
        PayrollMonth tested = payrollMonthFactory.findByDay(day);
        //Then
        Assert.assertEquals(firstDayOfMonth,tested.firstDay);

    }

}