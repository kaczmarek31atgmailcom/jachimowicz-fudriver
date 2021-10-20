package com.fungisearch.fudriver.payroll.salary.query.service;

import com.fungisearch.fudriver.payroll.calendar.command.model.Calendar;
import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.salary.query.dto.notPayed.PersonSalaryHeaderDto;
import com.fungisearch.fudriver.payroll.wage.command.model.WageHeader;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.testTools.*;
import com.fungisearch.fudriver.type.command.model.Type;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@ContextConfiguration(locations = "/test-spring.xml")
public class SalaryQueryServiceITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private NotPayedSalaryQueryService salaryQueryService;
    @Autowired
    private CreatePerson createPerson;
    @Autowired
    private CreateType createType;
    @Autowired
    private CreateCalendar createCalendar;
    @Autowired
    private CreateWageHeader createWageHeader;
    @Autowired
    private CreateZarobki createZarobki;
    @Autowired
    private FlushDao flushDao;
    @Autowired
    private CreateTimeWorkLog createTimeWorkLog;


    private final DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd");
    private final DateTimeFormatter dfl = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");

    @Test
    public void shouldReturnRegularEntry() {
        //Given
        Type type = createType.create();
        WageHeader wageHeader = createWageHeader.create("test");
        Person person = createPerson.create(wageHeader);
        person.switchToAccordAccordPayed();
        wageHeader.getRegularWage(type).updateValue(100L);
        Date day = df.parseDateTime("2017-03-04").toDate();
        createZarobki.create(type, day, person);
        createZarobki.create(type, day, person);
        Calendar calendar = createCalendar.create(day, SalaryDayTypeEnum.REGULAR_DAY);
        calendar.setRegularDay();
        flushDao.flush();
        //When
        List<PersonSalaryHeaderDto> tested = salaryQueryService.getHeaders(day);
        //Then
        assertEquals(1, tested.size());
        assertEquals(220L, tested.get(0).regularHarvestSalary);
        assertEquals((Long)person.getId(),(Long)tested.get(0).id);
    }

    @Test
    public void shouldReturnSundayEntry() {
        //Given
        Type type = createType.create();
        WageHeader wageHeader = createWageHeader.create("test");
        Person person = createPerson.create(wageHeader);
        person.switchToAccordAccordPayed();
        wageHeader.getSundayWage(type).updateValue(100L);
        Date day = df.parseDateTime("2017-03-04").toDate();
        createZarobki.create(type, day, person);
        Calendar calendar = createCalendar.create(day, SalaryDayTypeEnum.SUNDAY);
        calendar.setSunday();
        flushDao.flush();
        //When
        List<PersonSalaryHeaderDto> tested = salaryQueryService.getHeaders(day);
        //Then
        assertEquals(1, tested.size());
        assertEquals(110L, tested.get(0).sundayHarvestSalary);
        assertEquals((Long)person.getId(),(Long)tested.get(0).id);
    }

    @Test
    public void shouldReturnBonusEntry() {
        //Given
        Type type = createType.create();
        WageHeader wageHeader = createWageHeader.create("test");
        Person person = createPerson.create(wageHeader);
        person.switchToAccordAccordPayed();
        wageHeader.getBonusWage(type).updateValue(100L);
        Date day = df.parseDateTime("2017-03-04").toDate();
        createZarobki.create(type, day, person);
        createZarobki.create(type, day, person);
        Calendar calendar = createCalendar.create(day, SalaryDayTypeEnum.BONUS_DAY);
        calendar.setBonusDay();
        flushDao.flush();
        //When
        List<PersonSalaryHeaderDto> tested = salaryQueryService.getHeaders(day);
        //Then
        assertEquals(1, tested.size());
        assertEquals(220L, tested.get(0).bonusHarvestSalary);
        assertEquals((Long)person.getId(),(Long)tested.get(0).id);
    }

    @Test
    public void shouldNotReturnRegularHarvestForNonAccordEmployee() {
        //Given
        Type type = createType.create();
        WageHeader wageHeader = createWageHeader.create("test");
        Person person = createPerson.create(wageHeader);
        person.switchToHourlyPayed();
        wageHeader.getRegularWage(type).updateValue(100L);
        wageHeader.getSundayWage(type).updateValue(100L);
        wageHeader.getBonusWage(type).updateValue(100L);
        Date day = df.parseDateTime("2017-03-04").toDate();
        createZarobki.create(type, day, person);
        createZarobki.create(type, day, person);
        Calendar calendar = createCalendar.create(day, SalaryDayTypeEnum.REGULAR_DAY);
        calendar.setBonusDay();
        flushDao.flush();
        //When
        List<PersonSalaryHeaderDto> tested = salaryQueryService.getHeaders(day);
        //Then
        assertEquals(0, tested.size());
    }

    @Test
    public void shouldNotReturnSundayHarvestForNonAccordEmployee() {
        //Given
        Type type = createType.create();
        WageHeader wageHeader = createWageHeader.create("test");
        Person person = createPerson.create(wageHeader);
        person.switchToHourlyPayed();
        wageHeader.getRegularWage(type).updateValue(100L);
        wageHeader.getSundayWage(type).updateValue(100L);
        wageHeader.getBonusWage(type).updateValue(100L);
        Date day = df.parseDateTime("2017-03-04").toDate();
        createZarobki.create(type, day, person);
        createZarobki.create(type, day, person);
        Calendar calendar = createCalendar.create(day, SalaryDayTypeEnum.SUNDAY);
        calendar.setBonusDay();
        flushDao.flush();
        //When
        List<PersonSalaryHeaderDto> tested = salaryQueryService.getHeaders(day);
        //Then
        assertEquals(0, tested.size());
    }

    @Test
    public void shouldNotReturnBonusHarvestForNonAccordEmployee() {
        //Given
        Type type = createType.create();
        WageHeader wageHeader = createWageHeader.create("test");
        Person person = createPerson.create(wageHeader);
        person.switchToHourlyPayed();
        wageHeader.getRegularWage(type).updateValue(100L);
        wageHeader.getSundayWage(type).updateValue(100L);
        wageHeader.getBonusWage(type).updateValue(100L);
        Date day = df.parseDateTime("2017-03-04").toDate();
        createZarobki.create(type, day, person);
        createZarobki.create(type, day, person);
        Calendar calendar = createCalendar.create(day, SalaryDayTypeEnum.BONUS_DAY);
        calendar.setBonusDay();
        flushDao.flush();
        //When
        List<PersonSalaryHeaderDto> tested = salaryQueryService.getHeaders(day);
        //Then
        assertEquals(0, tested.size());
    }

    @Test
    public void shouldReturnRegularHoursSalary() {
        //Given
        Date startTime = dfl.parseDateTime("2017-03-04 08:00").toDate();
        Date endTime = dfl.parseDateTime("2017-03-04 10:00").toDate();
        Person person = createPerson.create();
        person.switchToHourlyPayed();
        person.setRegularWage(100L);
        person.setSundayWage(200L);
        person.setBonusWage(300L);
        Calendar calendar = createCalendar.create(startTime,SalaryDayTypeEnum.REGULAR_DAY);
        calendar.setRegularDay();
        createTimeWorkLog.create(startTime,endTime,person.getId());
        flushDao.flush();
        //When
        List<PersonSalaryHeaderDto> tested = salaryQueryService.getHeaders(startTime);
        //Then
        assertEquals(1,tested.size());
        assertEquals((Long)person.getId(),(Long)tested.get(0).id);
        assertEquals(120L,tested.get(0).regularMinutes);
        assertEquals(200L,tested.get(0).regularHoursSalary);
    }

    @Test
    public void shouldReturnSundayHoursSalary() {
        //Given
        Date startTime = dfl.parseDateTime("2017-03-04 08:00").toDate();
        Date endTime = dfl.parseDateTime("2017-03-04 10:00").toDate();
        Person person = createPerson.create();
        person.switchToHourlyPayed();
        person.setRegularWage(100L);
        person.setSundayWage(200L);
        person.setBonusWage(300L);
        Calendar calendar = createCalendar.create(startTime,SalaryDayTypeEnum.SUNDAY);
        calendar.setSunday();
        createTimeWorkLog.create(startTime,endTime,person.getId());
        flushDao.flush();
        //When
        List<PersonSalaryHeaderDto> tested = salaryQueryService.getHeaders(startTime);
        //Then
        assertEquals(1,tested.size());
        assertEquals((Long)person.getId(),(Long)tested.get(0).id);
        assertEquals(120L,tested.get(0).sundayMinutes);
        assertEquals(400L,tested.get(0).sundayHoursSalary);
    }

    @Test
    public void shouldReturnBonusHoursSalary() {
        //Given
        Date startTime = dfl.parseDateTime("2017-03-04 08:00").toDate();
        Date endTime = dfl.parseDateTime("2017-03-04 10:00").toDate();
        Person person = createPerson.create();
        person.switchToHourlyPayed();
        person.setRegularWage(100L);
        person.setSundayWage(200L);
        person.setBonusWage(300L);
        Calendar calendar = createCalendar.create(startTime,SalaryDayTypeEnum.BONUS_DAY);
        calendar.setBonusDay();
        createTimeWorkLog.create(startTime,endTime,person.getId());
        flushDao.flush();
        //When
        List<PersonSalaryHeaderDto> tested = salaryQueryService.getHeaders(startTime);
        //Then
        assertEquals(1,tested.size());
        assertEquals((Long)person.getId(),(Long)tested.get(0).id);
        assertEquals(120L,tested.get(0).bonusMinutes);
        assertEquals(600L,tested.get(0).bonusHoursSalary);
    }

    @Test
    public void shouldNotReturnHoursSalaryForAccordPerson() {
        //Given
        Date startTime = dfl.parseDateTime("2017-03-04 08:00").toDate();
        Date endTime = dfl.parseDateTime("2017-03-04 10:00").toDate();
        Person person = createPerson.create();
        person.switchToAccordAccordPayed();
        person.setRegularWage(100L);
        person.setSundayWage(200L);
        person.setBonusWage(300L);
        Calendar calendar = createCalendar.create(startTime,SalaryDayTypeEnum.BONUS_DAY);
        calendar.setBonusDay();
        createTimeWorkLog.create(startTime,endTime,person.getId());
        flushDao.flush();
        //When
        List<PersonSalaryHeaderDto> tested = salaryQueryService.getHeaders(startTime);
        //Then
        assertEquals(0,tested.size());
    }
}