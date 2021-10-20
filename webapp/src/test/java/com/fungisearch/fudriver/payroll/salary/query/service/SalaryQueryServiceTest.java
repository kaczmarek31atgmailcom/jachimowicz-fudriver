package com.fungisearch.fudriver.payroll.salary.query.service;

import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.salary.query.dao.SalaryDao;
import com.fungisearch.fudriver.payroll.salary.query.dto.notPayed.HarvestByPersonAndWageDto;
import com.fungisearch.fudriver.payroll.salary.query.dto.notPayed.PersonSalaryHeaderDto;
import com.fungisearch.fudriver.payroll.salary.query.dto.notPayed.WorkTimeByPersonAndDayTypeDto;
import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class SalaryQueryServiceTest {

    @Mock
    private SalaryDao salaryDao;

    @InjectMocks
    private NotPayedSalaryQueryService salaryQueryService;

    @Test
    public void shouldGetTimeShort() {
        //Given
        DateTimeFormatter df = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime dateTime = df.parseDateTime("20170304");
        //When
        String timshort = salaryQueryService.getTimeShort(dateTime.toDate());
        //Then
        assertEquals("201703", timshort);
    }

    @Test
    public void shouldCreateHarvestHeadersForRegularDay() {
        //Given
        List<PersonSalaryHeaderDto> names = new ArrayList<>();
        PersonSalaryHeaderDto person1 = new PersonSalaryHeaderDto();
        person1.id = 1L;
        person1.payrollType = PayrollTypeEnum.ACCORD;
        PersonSalaryHeaderDto person2 = new PersonSalaryHeaderDto();
        person2.id = 2L;
        person2.payrollType = PayrollTypeEnum.ACCORD;
        names.add(person1);
        names.add(person2);

        HarvestByPersonAndWageDto harvest1 = new HarvestByPersonAndWageDto();
        harvest1.personId = 1L;
        harvest1.dayType = SalaryDayTypeEnum.REGULAR_DAY;
        harvest1.salary = 100L;
        HarvestByPersonAndWageDto harvest2 = new HarvestByPersonAndWageDto();
        harvest2.personId = 1L;
        harvest2.dayType = SalaryDayTypeEnum.REGULAR_DAY;
        harvest2.salary = 100L;
        List<HarvestByPersonAndWageDto> harverstList = new ArrayList<>();
        harverstList.add(harvest1);
        harverstList.add(harvest2);
        //When
        List<PersonSalaryHeaderDto> tested = salaryQueryService.getHarvestHeaders(names,harverstList);
        //Then
        assertEquals(1, tested.size());
        assertEquals(200L, tested.get(0).regularHarvestSalary);
        assertEquals(1L, tested.get(0).id);
    }

    @Test
    public void shouldCreateHarvestHeadersForSunday() {
        //Given
        List<PersonSalaryHeaderDto> names = new ArrayList<>();
        PersonSalaryHeaderDto person1 = new PersonSalaryHeaderDto();
        person1.id = 1L;
        person1.payrollType = PayrollTypeEnum.ACCORD;
        PersonSalaryHeaderDto person2 = new PersonSalaryHeaderDto();
        person2.id = 2L;
        person2.payrollType = PayrollTypeEnum.ACCORD;
        names.add(person1);
        names.add(person2);

        HarvestByPersonAndWageDto harvest1 = new HarvestByPersonAndWageDto();
        harvest1.personId = 1L;
        harvest1.dayType = SalaryDayTypeEnum.SUNDAY;
        harvest1.salary = 100L;
        HarvestByPersonAndWageDto harvest2 = new HarvestByPersonAndWageDto();
        harvest2.personId = 1L;
        harvest2.dayType = SalaryDayTypeEnum.SUNDAY;
        harvest2.salary = 100L;
        List<HarvestByPersonAndWageDto> harverstList = new ArrayList<>();
        harverstList.add(harvest1);
        harverstList.add(harvest2);
        //When
        List<PersonSalaryHeaderDto> tested = salaryQueryService.getHarvestHeaders(names,harverstList);
        //Then
        assertEquals(1, tested.size());
        assertEquals(200L, tested.get(0).sundayHarvestSalary);
        assertEquals(1L, tested.get(0).id);
    }

    @Test
    public void shouldCreateHarvestHeadersForBonusDay() {
        //Given
        List<PersonSalaryHeaderDto> names = new ArrayList<>();
        PersonSalaryHeaderDto person1 = new PersonSalaryHeaderDto();
        person1.id = 1L;
        person1.payrollType = PayrollTypeEnum.ACCORD;
        PersonSalaryHeaderDto person2 = new PersonSalaryHeaderDto();
        person2.id = 2L;
        person2.payrollType = PayrollTypeEnum.ACCORD;
        names.add(person1);
        names.add(person2);

        HarvestByPersonAndWageDto harvest1 = new HarvestByPersonAndWageDto();
        harvest1.personId = 1L;
        harvest1.dayType = SalaryDayTypeEnum.BONUS_DAY;
        harvest1.salary = 100L;
        HarvestByPersonAndWageDto harvest2 = new HarvestByPersonAndWageDto();
        harvest2.personId = 1L;
        harvest2.dayType = SalaryDayTypeEnum.BONUS_DAY;
        harvest2.salary = 100L;
        List<HarvestByPersonAndWageDto> harverstList = new ArrayList<>();
        harverstList.add(harvest1);
        harverstList.add(harvest2);
        //When
        List<PersonSalaryHeaderDto> tested = salaryQueryService.getHarvestHeaders(names,harverstList);
        //Then
        assertEquals(1, tested.size());
        assertEquals(200L, tested.get(0).bonusHarvestSalary);
        assertEquals(1L, tested.get(0).id);
    }


    @Test
    public void shouldFindPersonInTheList() {
        //Given
        List<PersonSalaryHeaderDto> theList = new ArrayList<>();
        PersonSalaryHeaderDto dto1 = new PersonSalaryHeaderDto();
        dto1.id = 31L;
        PersonSalaryHeaderDto dto2 = new PersonSalaryHeaderDto();
        dto2.id = 2L;
        PersonSalaryHeaderDto dto3 = new PersonSalaryHeaderDto();
        dto3.id = 5L;
        PersonSalaryHeaderDto dto4 = new PersonSalaryHeaderDto();
        dto4.id = 1000L;
        PersonSalaryHeaderDto dto5 = new PersonSalaryHeaderDto();
        dto5.id = 1001L;
        theList.add(dto1);
        theList.add(dto2);
        theList.add(dto3);
        theList.add(dto4);
        theList.add(dto5);
        theList.add(dto5);
        //When & Then
        assertEquals(31L, salaryQueryService.findPerson(theList, 31L).id);
        assertEquals(2L, salaryQueryService.findPerson(theList, 2L).id);
        assertEquals(5L, salaryQueryService.findPerson(theList, 5L).id);
        assertEquals(1000L, salaryQueryService.findPerson(theList, 1000L).id);
        try {
            Assert.assertNull(salaryQueryService.findPerson(theList, 13L));
            fail();
        } catch (NoSuchElementException ex) {
        }
        try {
            Assert.assertNull(salaryQueryService.findPerson(theList, 1001L));
            fail();
        } catch (IllegalStateException ex) {
        }
    }


    @Test
    public void shouldJoinPersonWithHarvestSDataForRegularDay() {


        List<PersonSalaryHeaderDto> persons = new ArrayList<>();
        PersonSalaryHeaderDto person1 = new PersonSalaryHeaderDto();
        person1.id = 1L;
        person1.payrollType = PayrollTypeEnum.ACCORD;
        PersonSalaryHeaderDto person2 = new PersonSalaryHeaderDto();
        person2.id = 2L;
        person2.payrollType = PayrollTypeEnum.ACCORD;
        persons.add(person1);
        persons.add(person2);
        List<HarvestByPersonAndWageDto> harvestData = new ArrayList<>();
        HarvestByPersonAndWageDto harvest1 = new HarvestByPersonAndWageDto();
        harvest1.personId = 1L;
        harvest1.salary = 3000L;
        harvest1.dayType = SalaryDayTypeEnum.REGULAR_DAY;
        HarvestByPersonAndWageDto harvest2 = new HarvestByPersonAndWageDto();
        harvest2.personId = 1L;
        harvest2.salary = 5000L;
        harvest2.dayType = SalaryDayTypeEnum.REGULAR_DAY;
        harvestData.add(harvest1);
        harvestData.add(harvest2);

        //When
        List<PersonSalaryHeaderDto> result = salaryQueryService.getHarvestHeaders(persons,harvestData);
        //Then
        assertEquals(1, result.size());
        assertEquals(8000L, result.get(0).regularHarvestSalary);
    }

    @Test
    public void shouldJoinPersonWithHarvestSDataForSunday() {
        List<PersonSalaryHeaderDto> persons = new ArrayList<>();
        PersonSalaryHeaderDto person1 = new PersonSalaryHeaderDto();
        person1.id = 1L;
        person1.payrollType = PayrollTypeEnum.ACCORD;
        PersonSalaryHeaderDto person2 = new PersonSalaryHeaderDto();
        person2.id = 2L;
        person2.payrollType = PayrollTypeEnum.ACCORD;
        persons.add(person1);
        persons.add(person2);
        List<HarvestByPersonAndWageDto> harvestData = new ArrayList<>();
        HarvestByPersonAndWageDto harvest1 = new HarvestByPersonAndWageDto();
        harvest1.personId = 1L;
        harvest1.salary = 3000L;
        harvest1.dayType = SalaryDayTypeEnum.SUNDAY;
        HarvestByPersonAndWageDto harvest2 = new HarvestByPersonAndWageDto();
        harvest2.personId = 1L;
        harvest2.salary = 3000L;
        harvest2.dayType = SalaryDayTypeEnum.SUNDAY;
        harvestData.add(harvest1);
        harvestData.add(harvest2);

        //When
        List<PersonSalaryHeaderDto> result = salaryQueryService.getHarvestHeaders(persons,harvestData);
        //Then
        assertEquals(1, result.size());
        assertEquals(6000L, result.get(0).sundayHarvestSalary);
    }

    @Test
    public void shouldJoinPersonWithHarvestSDataForAllDayTypes() {
        List<PersonSalaryHeaderDto> persons = new ArrayList<>();
        PersonSalaryHeaderDto person1 = new PersonSalaryHeaderDto();
        person1.id = 1L;
        person1.payrollType = PayrollTypeEnum.ACCORD;
        PersonSalaryHeaderDto person2 = new PersonSalaryHeaderDto();
        person2.id = 2L;
        person2.payrollType = PayrollTypeEnum.ACCORD;
        persons.add(person1);
        persons.add(person2);
        List<HarvestByPersonAndWageDto> harvestData = new ArrayList<>();
        HarvestByPersonAndWageDto harvest1 = new HarvestByPersonAndWageDto();
        harvest1.personId = 1L;
        harvest1.salary = 3000L;
        harvest1.dayType = SalaryDayTypeEnum.REGULAR_DAY;
        HarvestByPersonAndWageDto harvest2 = new HarvestByPersonAndWageDto();
        harvest2.personId = 1L;
        harvest2.salary = 3000L;
        harvest2.dayType = SalaryDayTypeEnum.REGULAR_DAY;
        HarvestByPersonAndWageDto harvest3 = new HarvestByPersonAndWageDto();
        harvest3.personId = 1L;
        harvest3.salary = 3000L;
        harvest3.dayType = SalaryDayTypeEnum.SUNDAY;
        HarvestByPersonAndWageDto harvest4 = new HarvestByPersonAndWageDto();
        harvest4.personId = 1L;
        harvest4.salary = 2000L;
        harvest4.dayType = SalaryDayTypeEnum.SUNDAY;
        HarvestByPersonAndWageDto harvest5 = new HarvestByPersonAndWageDto();
        harvest5.personId = 1L;
        harvest5.salary = 1000L;
        harvest5.dayType = SalaryDayTypeEnum.BONUS_DAY;
        HarvestByPersonAndWageDto harvest6 = new HarvestByPersonAndWageDto();
        harvest6.personId = 1L;
        harvest6.salary = 100L;
        harvest6.dayType = SalaryDayTypeEnum.BONUS_DAY;
        harvestData.add(harvest1);
        harvestData.add(harvest2);
        harvestData.add(harvest3);
        harvestData.add(harvest4);
        harvestData.add(harvest5);
        harvestData.add(harvest6);

        //When
        List<PersonSalaryHeaderDto> result = salaryQueryService.getHarvestHeaders(persons,harvestData);
        //Then
        assertEquals(1, result.size());
        assertEquals(6000L, result.get(0).regularHarvestSalary);
        assertEquals(5000L, result.get(0).sundayHarvestSalary);
        assertEquals(1100L, result.get(0).bonusHarvestSalary);
    }

    @Test
    public void shouldNotJoinPersonWithHarvestForHourlyPayedPerson(){
        List<PersonSalaryHeaderDto> persons = new ArrayList<>();
        PersonSalaryHeaderDto person1 = new PersonSalaryHeaderDto();
        person1.id = 1L;
        person1.payrollType = PayrollTypeEnum.HOURLY;
        PersonSalaryHeaderDto person2 = new PersonSalaryHeaderDto();
        person2.id = 2L;
        person2.payrollType = PayrollTypeEnum.ACCORD;
        persons.add(person1);
        persons.add(person2);
        List<HarvestByPersonAndWageDto> harvestData = new ArrayList<>();
        HarvestByPersonAndWageDto harvest1 = new HarvestByPersonAndWageDto();
        harvest1.personId = 1L;
        harvest1.salary = 3000L;
        harvest1.dayType = SalaryDayTypeEnum.REGULAR_DAY;
        HarvestByPersonAndWageDto harvest2 = new HarvestByPersonAndWageDto();
        harvest2.personId = 1L;
        harvest2.salary = 3000L;
        harvest2.dayType = SalaryDayTypeEnum.REGULAR_DAY;
        HarvestByPersonAndWageDto harvest3 = new HarvestByPersonAndWageDto();
        harvest3.personId = 1L;
        harvest3.salary = 3000L;
        harvest3.dayType = SalaryDayTypeEnum.SUNDAY;
        HarvestByPersonAndWageDto harvest4 = new HarvestByPersonAndWageDto();
        harvest4.personId = 1L;
        harvest4.salary = 2000L;
        harvest4.dayType = SalaryDayTypeEnum.SUNDAY;
        HarvestByPersonAndWageDto harvest5 = new HarvestByPersonAndWageDto();
        harvest5.personId = 1L;
        harvest5.salary = 1000L;
        harvest5.dayType = SalaryDayTypeEnum.BONUS_DAY;
        HarvestByPersonAndWageDto harvest6 = new HarvestByPersonAndWageDto();
        harvest6.personId = 1L;
        harvest6.salary = 100L;
        harvest6.dayType = SalaryDayTypeEnum.BONUS_DAY;
        harvestData.add(harvest1);
        harvestData.add(harvest2);
        harvestData.add(harvest3);
        harvestData.add(harvest4);
        harvestData.add(harvest5);
        harvestData.add(harvest6);

        //When
        List<PersonSalaryHeaderDto> result = salaryQueryService.getHarvestHeaders(persons,harvestData);
        //Then
        assertEquals(0, result.size());
    }

    @Test
    public void shouldFindMinutesBetweenDates(){
        //Given
        DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        Date start = df.parseDateTime("2017-03-01 08:00").toDate();
        Date end = df.parseDateTime("2017-03-01 10:15").toDate();
        //When
        int tested = salaryQueryService.findMinutes(start,end);
        //Then
        assertEquals(135L,tested);
    }

    @Test
    public void shouldJoinWorkTimeSalary(){
        //Given
        PersonSalaryHeaderDto person1 = new PersonSalaryHeaderDto();
        person1.id = 1L;
        person1.payrollType = PayrollTypeEnum.HOURLY;
        person1.regularHoursWage = 100L;
        person1.sundayHoursWage = 200L;
        person1.bonusHoursWage = 300L;
        PersonSalaryHeaderDto person2 = new PersonSalaryHeaderDto();
        person2.id = 2L;
        person2.payrollType = PayrollTypeEnum.HOURLY;
        List<PersonSalaryHeaderDto> names = new ArrayList<>();
        names.add(person1);
        names.add(person2);

        DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");

        WorkTimeByPersonAndDayTypeDto workTime1 = new WorkTimeByPersonAndDayTypeDto();
        workTime1.personId = 1L;
        workTime1.dayType = SalaryDayTypeEnum.REGULAR_DAY;
        workTime1.startTime = df.parseDateTime("2017-03-01 10:00").toDate();
        workTime1.endTime = df.parseDateTime("2017-03-01 12:00").toDate();

        List<WorkTimeByPersonAndDayTypeDto> workHours = new ArrayList<>();
        workHours.add(workTime1);
        //When
        List<PersonSalaryHeaderDto> tested = salaryQueryService.getHoursHeaders(names,workHours);
        //Then
        assertEquals(1,tested.size());
        assertEquals(120L,tested.get(0).regularMinutes);
        assertEquals(200L,tested.get(0).regularHoursSalary);
    }

    @Test
    public void shouldJoinTwoRegularWorkTimeSalaries(){
        //Given
        PersonSalaryHeaderDto person1 = new PersonSalaryHeaderDto();
        person1.id = 1L;
        person1.payrollType = PayrollTypeEnum.HOURLY;
        person1.regularHoursWage = 100L;
        person1.sundayHoursWage = 200L;
        person1.bonusHoursWage = 300L;
        PersonSalaryHeaderDto person2 = new PersonSalaryHeaderDto();
        person2.id = 2L;
        person2.payrollType = PayrollTypeEnum.HOURLY;
        List<PersonSalaryHeaderDto> names = new ArrayList<>();
        names.add(person1);
        names.add(person2);

        DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");

        WorkTimeByPersonAndDayTypeDto workTime1 = new WorkTimeByPersonAndDayTypeDto();
        workTime1.personId = 1L;
        workTime1.dayType = SalaryDayTypeEnum.REGULAR_DAY;
        workTime1.startTime = df.parseDateTime("2017-03-01 10:00").toDate();
        workTime1.endTime = df.parseDateTime("2017-03-01 12:00").toDate();

        WorkTimeByPersonAndDayTypeDto workTime2 = new WorkTimeByPersonAndDayTypeDto();
        workTime2.personId = 1L;
        workTime2.dayType = SalaryDayTypeEnum.REGULAR_DAY;
        workTime2.startTime = df.parseDateTime("2017-03-01 14:00").toDate();
        workTime2.endTime = df.parseDateTime("2017-03-01 16:00").toDate();

        List<WorkTimeByPersonAndDayTypeDto> workHours = new ArrayList<>();
        workHours.add(workTime1);
        workHours.add(workTime2);
        //When
        List<PersonSalaryHeaderDto> tested = salaryQueryService.getHoursHeaders(names,workHours);
        //Then
        assertEquals(1,tested.size());
        assertEquals(240L, tested.get(0).regularMinutes);
        assertEquals(400L, tested.get(0).regularHoursSalary);
    }

    @Test
    public void shouldJoinTwoSundayWorkTimeSalaries(){
        //Given
        PersonSalaryHeaderDto person1 = new PersonSalaryHeaderDto();
        person1.id = 4L;
        person1.payrollType = PayrollTypeEnum.HOURLY;
        person1.regularHoursWage = 100L;
        person1.sundayHoursWage = 200L;
        person1.bonusHoursWage = 300L;
        PersonSalaryHeaderDto person2 = new PersonSalaryHeaderDto();
        person2.id = 2L;
        person2.payrollType = PayrollTypeEnum.HOURLY;
        List<PersonSalaryHeaderDto> names = new ArrayList<>();
        names.add(person1);
        names.add(person2);

        DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");

        WorkTimeByPersonAndDayTypeDto workTime1 = new WorkTimeByPersonAndDayTypeDto();
        workTime1.personId = 4L;
        workTime1.dayType = SalaryDayTypeEnum.SUNDAY;
        workTime1.startTime = df.parseDateTime("2017-03-01 10:00").toDate();
        workTime1.endTime = df.parseDateTime("2017-03-01 12:00").toDate();

        WorkTimeByPersonAndDayTypeDto workTime2 = new WorkTimeByPersonAndDayTypeDto();
        workTime2.personId = 4L;
        workTime2.dayType = SalaryDayTypeEnum.SUNDAY;
        workTime2.startTime = df.parseDateTime("2017-03-01 14:00").toDate();
        workTime2.endTime = df.parseDateTime("2017-03-01 16:00").toDate();

        List<WorkTimeByPersonAndDayTypeDto> workHours = new ArrayList<>();
        workHours.add(workTime1);
        workHours.add(workTime2);
        //When
        List<PersonSalaryHeaderDto> tested = salaryQueryService.getHoursHeaders(names,workHours);
        //Then
        assertEquals(1,tested.size());
        assertEquals(240L, tested.get(0).sundayMinutes);
        assertEquals(800L,tested.get(0).sundayHoursSalary);
    }

    @Test
    public void shouldJoinTwoBonusWorkTimeSalaries(){
        //Given
        PersonSalaryHeaderDto person1 = new PersonSalaryHeaderDto();
        person1.id = 4L;
        person1.payrollType = PayrollTypeEnum.HOURLY;
        person1.regularHoursWage = 100L;
        person1.sundayHoursWage = 200L;
        person1.bonusHoursWage = 300L;
        PersonSalaryHeaderDto person2 = new PersonSalaryHeaderDto();
        person2.id = 2L;
        person2.payrollType = PayrollTypeEnum.HOURLY;
        List<PersonSalaryHeaderDto> names = new ArrayList<>();
        names.add(person1);
        names.add(person2);

        DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");

        WorkTimeByPersonAndDayTypeDto workTime1 = new WorkTimeByPersonAndDayTypeDto();
        workTime1.personId = 4L;
        workTime1.dayType = SalaryDayTypeEnum.BONUS_DAY;
        workTime1.startTime = df.parseDateTime("2017-03-01 10:00").toDate();
        workTime1.endTime = df.parseDateTime("2017-03-01 12:00").toDate();

        WorkTimeByPersonAndDayTypeDto workTime2 = new WorkTimeByPersonAndDayTypeDto();
        workTime2.personId = 4L;
        workTime2.dayType = SalaryDayTypeEnum.BONUS_DAY;
        workTime2.startTime = df.parseDateTime("2017-03-01 14:00").toDate();
        workTime2.endTime = df.parseDateTime("2017-03-01 16:00").toDate();

        List<WorkTimeByPersonAndDayTypeDto> workHours = new ArrayList<>();
        workHours.add(workTime1);
        workHours.add(workTime2);
        //When
        List<PersonSalaryHeaderDto> tested = salaryQueryService.getHoursHeaders(names,workHours);
        //Then
        assertEquals(1,tested.size());
        assertEquals(240L, tested.get(0).bonusMinutes);
        assertEquals(1200L,tested.get(0).bonusHoursSalary);
    }

}