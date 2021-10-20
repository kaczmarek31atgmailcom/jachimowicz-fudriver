package com.fungisearch.fudriver.payroll.salary.command.model;

import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollMonthRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class PayrollMonthTest {

    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Mock
    private BeanValidator beanValidator;
    @Mock
    private PayrollMonthRepository payrollMonthRepository;


    @Test
    public void shouldConfirmDayInTheSameMonth(){
        //Given
        Date firstDayOfMonth = formatter.parseDateTime("2017-01-01").toDate();
        Date testedDay = formatter.parseDateTime("2017-01-12").toDate();
        PayrollMonth payrollMonth = new PayrollMonth.PayrollMonthBuilder(payrollMonthRepository,beanValidator).firstDay(firstDayOfMonth).build();
        payrollMonth.id = 1L;
        //When & Then
        assertTrue(payrollMonth.isDayInMonth(testedDay));
    }

    @Test
    public void shouldNotConfirmDayInTheSameMonth(){
        //Given
        Date firstDayOfMonth = formatter.parseDateTime("2017-01-01").toDate();
        Date testedDay = formatter.parseDateTime("2017-02-12").toDate();
        PayrollMonth payrollMonth = new PayrollMonth.PayrollMonthBuilder(payrollMonthRepository,beanValidator).firstDay(firstDayOfMonth).build();
        payrollMonth.id = 1L;
        //When & Then
        assertFalse(payrollMonth.isDayInMonth(testedDay));
    }

    @Test
    public void shouldCloseMonth(){
        Date firstDayOfMonth = formatter.parseDateTime("2017-01-01").toDate();
        Date testedDay = formatter.parseDateTime("2017-02-12").toDate();
        PayrollMonth payrollMonth = new PayrollMonth.PayrollMonthBuilder(payrollMonthRepository,beanValidator).firstDay(firstDayOfMonth).build();
        payrollMonth.id = 1L;
        //When
        payrollMonth.close();
        //Then
        assertTrue(payrollMonth.isClosed);
    }

}