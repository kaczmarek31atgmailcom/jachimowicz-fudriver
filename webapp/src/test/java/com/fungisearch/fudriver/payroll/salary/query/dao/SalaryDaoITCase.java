package com.fungisearch.fudriver.payroll.salary.query.dao;

import com.fungisearch.fudriver.payroll.calendar.command.model.Calendar;
import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.salary.query.dto.notPayed.HarvestByPersonAndWageDto;
import com.fungisearch.fudriver.payroll.wage.command.model.Wage;
import com.fungisearch.fudriver.payroll.wage.command.model.WageHeader;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.testTools.*;
import com.fungisearch.fudriver.type.command.model.Type;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;

import static org.junit.Assert.*;

@ContextConfiguration(locations = "/test-spring.xml")
public class SalaryDaoITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CreatePerson createPerson;
    @Autowired
    private CreateWageHeader createWageHeader;
    @Autowired
    private CreateType createType;
    @Autowired
    private CreateWage createWage;
    @Autowired
    private CreateZarobki createZarobki;
    @Autowired
    private FlushDao flushDao;
    @Autowired
    private SalaryDao salaryDao;
    @Autowired
    private CreateCalendar createCalendar;

    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");


    @Test
    public void shouldCountSingleRegularSalary() {
        //Given
        DateTime dateTime1 = formatter.parseDateTime("20170301");
        Type type = createType.create();
        Calendar day = createCalendar.create(dateTime1.toDate(), SalaryDayTypeEnum.REGULAR_DAY);
        WageHeader wageHeader = createWageHeader.create("test_wage_header");
        createWage.createRegularWage(wageHeader, type, 100L);
        Person person = createPerson.create(wageHeader);
        createZarobki.create(type, dateTime1.toDate(), person);
        flushDao.flush();
        //When
        List<HarvestByPersonAndWageDto> tested = salaryDao.getHarvestByPersonAndWage("201703");
        //Then
        assertEquals(1, tested.size());
        assertEquals(110L, tested.get(0).salary);
        assertEquals(1100L, tested.get(0).totalKg);
        assertEquals(person.getId(),(Long)tested.get(0).personId);
        assertEquals(SalaryDayTypeEnum.REGULAR_DAY,tested.get(0).dayType);
    }

    @Test
    public void shouldCountTwoSalaryEntriesForRegularDay() {
        //Given
        DateTime dateTime1 = formatter.parseDateTime("20170301");
        DateTime dateTime2 = formatter.parseDateTime("20170302");
        Type type = createType.create();
        createCalendar.create(dateTime1.toDate(), SalaryDayTypeEnum.REGULAR_DAY);
        createCalendar.create(dateTime2.toDate(), SalaryDayTypeEnum.REGULAR_DAY);
        WageHeader wageHeader = createWageHeader.create("test_wage_header");
        createWage.createRegularWage(wageHeader, type, 100L);
        Person person = createPerson.create(wageHeader);
        createZarobki.create(type, dateTime1.toDate(), person);
        createZarobki.create(type, dateTime2.toDate(), person);
        flushDao.flush();
        //When
        List<HarvestByPersonAndWageDto> tested = salaryDao.getHarvestByPersonAndWage("201703");
        //Then
        assertEquals(1, tested.size());
        assertEquals(220L, tested.get(0).salary);
        assertEquals(2200L, tested.get(0).totalKg);
        assertEquals(person.getId(),(Long)tested.get(0).personId);
        assertEquals(SalaryDayTypeEnum.REGULAR_DAY,tested.get(0).dayType);
    }

    @Test
    public void shouldCountSingleSundaySalary() {
        //Given
        DateTime dateTime1 = formatter.parseDateTime("20170301");
        Type type = createType.create();
        Calendar day = createCalendar.create(dateTime1.toDate(), SalaryDayTypeEnum.SUNDAY);
        day.setSunday();
        WageHeader wageHeader = createWageHeader.create("test_wage_header");
        Wage wage  = createWage.createSundayWage(wageHeader, type, 100L);
        Person person = createPerson.create(wageHeader);
        createZarobki.create(type, dateTime1.toDate(), person);
        flushDao.flush();
        //When
        List<HarvestByPersonAndWageDto> tested = salaryDao.getHarvestByPersonAndWage("201703");
        //Then
        assertEquals(1, tested.size());
        assertEquals(110L, tested.get(0).salary);
        assertEquals(1100L, tested.get(0).totalKg);
        assertEquals(person.getId(),(Long)tested.get(0).personId);
        assertEquals(SalaryDayTypeEnum.SUNDAY,tested.get(0).dayType);
    }
}