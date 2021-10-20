package com.fungisearch.fudriver.payroll.salary.query.service;

import com.fungisearch.fudriver.payroll.salary.query.dao.SalaryDao;
import com.fungisearch.fudriver.payroll.salary.query.dto.notPayed.PersonWorkTimeDto;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkTimeCheckerTest {

    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    @Mock
    private SalaryDao salaryDao;

    @InjectMocks
    private WorkTimeChecker workTimeChecker;

    @Test
    public void shouldReturnPersonIdWithTwoPeriodsInTheSameTime() {
        //Given
        List<PersonWorkTimeDto> input = new ArrayList<>();
        input.addAll(getTwoOverlapingPeriods(1L));
        when(salaryDao.getMonthlyWorkTime(any(Date.class), any(Date.class))).thenReturn(input);
        //when
        List<Long> tested = workTimeChecker.findPeopleWithInvalidWorkTime(formatter.parseDateTime("2017-02-02 10:00:00").toDate());
        //Then
        assertEquals(new Long(1),tested.get(0));
    }

    @Test
    public void shouldReturnTwoPersonIdsWithOverlappingPeriods(){
        //Given
        List<PersonWorkTimeDto> input = new ArrayList<>();
        input.addAll(getTwoOverlapingPeriods(1L));
        input.addAll(getTwoOverlapingPeriods(2L));
        when(salaryDao.getMonthlyWorkTime(any(Date.class), any(Date.class))).thenReturn(input);
        //When
        List<Long> tested = workTimeChecker.findPeopleWithInvalidWorkTime(formatter.parseDateTime("2017-02-02 10:00:00").toDate());
        //Then
        assertEquals(2,tested.size());
        assertEquals(new Long(1),tested.get(0));
        assertEquals(new Long(2),tested.get(1));
    }

    @Test
    public void shouldReturnOnePersonIdsWithOverlappingPeriods(){
        //Given
        List<PersonWorkTimeDto> input = new ArrayList<>();
        input.addAll(getTwoOverlapingPeriods(1L));
        input.addAll(getTwoNotOverlappingPeriods(2L));
        when(salaryDao.getMonthlyWorkTime(any(Date.class), any(Date.class))).thenReturn(input);
        //When
        List<Long> tested = workTimeChecker.findPeopleWithInvalidWorkTime(formatter.parseDateTime("2017-02-02 10:00:00").toDate());
        //Then
        assertEquals(1,tested.size());
        assertEquals(new Long(1), tested.get(0));
    }

    @Test
    public void shouldReturnOnePersonIdsWithNotClosedPeriod(){
        //Given
        List<PersonWorkTimeDto> input = new ArrayList<>();
        input.addAll(getTwoNotOverlappingPeriods(1L));
        input.addAll(getTwoNotClashingOneOpenPeriods(2L));
        input.addAll(getTwoNotOverlappingPeriods(3L));

        when(salaryDao.getMonthlyWorkTime(any(Date.class), any(Date.class))).thenReturn(input);
        //When
        List<Long> tested = workTimeChecker.findPeopleWithInvalidWorkTime(formatter.parseDateTime("2017-02-02 10:00:00").toDate());
        //Then
        assertEquals(1,tested.size());
        assertEquals(new Long(2),tested.get(0));
    }

    @Test
    public void shouldReturnOnePersonIdsWithStartAndEndDateInDifferentDays(){
        //Given
        List<PersonWorkTimeDto> input = new ArrayList<>();
        input.addAll(getTwoNotOverlappingPeriods(1L));
        input.addAll(getTwoNotOverlappingPeriods(3L));
        DateTime start1 = formatter.parseDateTime("2017-02-02 10:00:00");
        DateTime end1 = formatter.parseDateTime("2017-02-03 12:00:00");
        PersonWorkTimeDto dto = new PersonWorkTimeDto();
        dto.personId = 2L;
        dto.startTime = start1.toDate();
        dto.endTime = end1.toDate();
        dto.isClosed = true;
        input.add(dto);
        when(salaryDao.getMonthlyWorkTime(any(Date.class), any(Date.class))).thenReturn(input);
        //When
        List<Long> tested = workTimeChecker.findPeopleWithInvalidWorkTime(formatter.parseDateTime("2017-02-02 10:00:00").toDate());
        //Then
        assertEquals(1,tested.size());
        assertEquals(new Long(2),tested.get(0));
    }

    private List<PersonWorkTimeDto> getTwoOverlapingPeriods(long personId) {
        DateTime start1 = formatter.parseDateTime("2017-02-02 10:00:00");
        DateTime end1 = formatter.parseDateTime("2017-02-02 12:00:00");
        PersonWorkTimeDto dto1 = new PersonWorkTimeDto();
        dto1.personId = personId;
        dto1.startTime = start1.toDate();
        dto1.endTime = end1.toDate();
        dto1.isClosed = true;

        DateTime start2 = formatter.parseDateTime("2017-02-02 11:00:00");
        DateTime end2 = formatter.parseDateTime("2017-02-02 13:00:00");
        PersonWorkTimeDto dto2 = new PersonWorkTimeDto();
        dto2.personId = personId;
        dto2.startTime = start2.toDate();
        dto2.endTime = end2.toDate();
        dto2.isClosed = true;

        List<PersonWorkTimeDto> workTimeList = new ArrayList<>();
        workTimeList.add(dto1);
        workTimeList.add(dto2);
        return workTimeList;
    }

    private List<PersonWorkTimeDto> getTwoNotOverlappingPeriods(long personId) {
        DateTime start1 = formatter.parseDateTime("2017-02-02 10:00:00");
        DateTime end1 = formatter.parseDateTime("2017-02-02 12:00:00");
        PersonWorkTimeDto dto1 = new PersonWorkTimeDto();
        dto1.personId = personId;
        dto1.startTime = start1.toDate();
        dto1.endTime = end1.toDate();
        dto1.isClosed = true;

        DateTime start2 = formatter.parseDateTime("2017-02-02 13:00:00");
        DateTime end2 = formatter.parseDateTime("2017-02-02 14:00:00");
        PersonWorkTimeDto dto2 = new PersonWorkTimeDto();
        dto2.personId = personId;
        dto2.startTime = start2.toDate();
        dto2.endTime = end2.toDate();
        dto2.isClosed = true;

        List<PersonWorkTimeDto> workTimeList = new ArrayList<>();
        workTimeList.add(dto1);
        workTimeList.add(dto2);
        return workTimeList;
    }

    private List<PersonWorkTimeDto> getTwoNotClashingOneOpenPeriods(long personId) {
        DateTime start1 = formatter.parseDateTime("2017-02-02 10:00:00");
        DateTime end1 = formatter.parseDateTime("2017-02-02 12:00:00");
        PersonWorkTimeDto dto1 = new PersonWorkTimeDto();
        dto1.personId = personId;
        dto1.startTime = start1.toDate();
        dto1.endTime = end1.toDate();
        dto1.isClosed = true;

        DateTime start2 = formatter.parseDateTime("2017-02-02 13:00:00");
        DateTime end2 = formatter.parseDateTime("2017-02-02 14:00:00");
        PersonWorkTimeDto dto2 = new PersonWorkTimeDto();
        dto2.personId = personId;
        dto2.startTime = start2.toDate();
        dto2.endTime = end2.toDate();
        dto2.isClosed = false;

        List<PersonWorkTimeDto> workTimeList = new ArrayList<>();
        workTimeList.add(dto1);
        workTimeList.add(dto2);
        return workTimeList;
    }

}