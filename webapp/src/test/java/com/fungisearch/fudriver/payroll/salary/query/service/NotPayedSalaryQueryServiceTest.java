package com.fungisearch.fudriver.payroll.salary.query.service;

import com.fungisearch.fudriver.payroll.calendar.command.model.Calendar;
import com.fungisearch.fudriver.payroll.calendar.command.model.CalendarFactory;
import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.calendar.command.repository.CalendarRepository;
import com.fungisearch.fudriver.payroll.salary.query.dao.SalaryDao;
import com.fungisearch.fudriver.payroll.salary.query.dto.notPayed.PersonSalaryHeaderDto;
import com.fungisearch.fudriver.payroll.salary.query.dto.notPayed.WorkTimeByPersonAndDayTypeDto;
import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnitRunner.class)
public class NotPayedSalaryQueryServiceTest {

    @Mock
    private SalaryDao salaryDao;
    @Mock
    private CalendarFactory calendarFactory;
    @Mock
    private CalendarRepository calendarRepository;
    @Mock
    private BeanValidator beanValidator;
    @InjectMocks
    private NotPayedSalaryQueryService notPayedSalaryQueryService;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public void shouldCountHoursSalaryForSingleReqularPeriod() {
        //Given
        Date day = Date.from(LocalDateTime.parse("2017-12-01 08:00:00", formatter).atZone(ZoneId.systemDefault()).toInstant());
        Calendar calendar = new Calendar.CalendarBuilder(calendarRepository, beanValidator)
                .date(day)
                .salaryDayType(SalaryDayTypeEnum.REGULAR_DAY)
                .build();
        Date day2 = Date.from(LocalDateTime.parse("2017-12-02 08:00:00", formatter).atZone(ZoneId.systemDefault()).toInstant());
        Calendar calendar2 = new Calendar.CalendarBuilder(calendarRepository, beanValidator)
                .date(day2)
                .salaryDayType(SalaryDayTypeEnum.REGULAR_DAY)
                .build();
        Date day3 = Date.from(LocalDateTime.parse("2017-12-03 08:00:00", formatter).atZone(ZoneId.systemDefault()).toInstant());
        Calendar calendar3 = new Calendar.CalendarBuilder(calendarRepository, beanValidator)
                .date(day3)
                .salaryDayType(SalaryDayTypeEnum.REGULAR_DAY)
                .build();
        when(calendarFactory.findDay(day)).thenReturn(calendar);
        when(calendarFactory.findDay(day2)).thenReturn(calendar2);
        when(calendarFactory.findDay(day3)).thenReturn(calendar3);
        when(salaryDao.getWorkTimeByPersonAndDayType(any(String.class))).thenReturn(getTwoPeriods());
        when(salaryDao.getPersonNames(any(String.class))).thenReturn(getHoursPeople());
        //When
        List<PersonSalaryHeaderDto> tested = notPayedSalaryQueryService.getHeaders(day);
        Collections.sort(tested,(o1,o2) ->new Long(o1.id).compareTo(new Long(o2.id)));
        //Then
        assertEquals(2000L, tested.get(0).regularHoursSalary);
        assertEquals(3000L,tested.get(1).regularHoursSalary);
    }


    private List<WorkTimeByPersonAndDayTypeDto> getTwoPeriods() {
        WorkTimeByPersonAndDayTypeDto dto = new WorkTimeByPersonAndDayTypeDto();
        dto.dayType = SalaryDayTypeEnum.REGULAR_DAY;
        dto.personId = 1L;
        dto.startTime = Date.from(LocalDateTime.parse("2017-12-01 08:00:00", formatter).atZone(ZoneId.systemDefault()).toInstant());
        dto.endTime = Date.from(LocalDateTime.parse("2017-12-01 10:00:00", formatter).atZone(ZoneId.systemDefault()).toInstant());

        WorkTimeByPersonAndDayTypeDto dto1 = new WorkTimeByPersonAndDayTypeDto();
        dto1.dayType = SalaryDayTypeEnum.REGULAR_DAY;
        dto1.personId = 1L;
        dto1.startTime = Date.from(LocalDateTime.parse("2017-12-01 11:00:00", formatter).atZone(ZoneId.systemDefault()).toInstant());
        dto1.endTime = Date.from(LocalDateTime.parse("2017-12-01 12:00:00", formatter).atZone(ZoneId.systemDefault()).toInstant());

        WorkTimeByPersonAndDayTypeDto dto2 = new WorkTimeByPersonAndDayTypeDto();
        dto2.dayType = SalaryDayTypeEnum.REGULAR_DAY;
        dto2.personId = 2L;
        dto2.startTime = Date.from(LocalDateTime.parse("2017-12-02 08:00:00", formatter).atZone(ZoneId.systemDefault()).toInstant());
        dto2.endTime = Date.from(LocalDateTime.parse("2017-12-02 10:00:00", formatter).atZone(ZoneId.systemDefault()).toInstant());

        WorkTimeByPersonAndDayTypeDto dto3 = new WorkTimeByPersonAndDayTypeDto();
        dto3.dayType = SalaryDayTypeEnum.REGULAR_DAY;
        dto3.personId = 2L;
        dto3.startTime = Date.from(LocalDateTime.parse("2017-12-03 11:00:00", formatter).atZone(ZoneId.systemDefault()).toInstant());
        dto3.endTime = Date.from(LocalDateTime.parse("2017-12-03 12:00:00", formatter).atZone(ZoneId.systemDefault()).toInstant());

        List<WorkTimeByPersonAndDayTypeDto> result = new ArrayList<>();
        result.add(dto);
        result.add(dto1);
        result.add(dto2);
        result.add(dto3);
        return result;
    }

    private List<PersonSalaryHeaderDto> getHoursPeople(){
        List<PersonSalaryHeaderDto> list = new ArrayList<>();
        PersonSalaryHeaderDto dto = new PersonSalaryHeaderDto();
        dto.id = 1L;
        dto.payrollType = PayrollTypeEnum.HOURLY;
        dto.regularHoursWage = 500L;
        dto.sundayHoursWage = 1000L;
        dto.bonusHoursWage = 1500L;

        PersonSalaryHeaderDto dto2 = new PersonSalaryHeaderDto();
        dto2.id = 2L;
        dto2.payrollType = PayrollTypeEnum.HOURLY;
        dto2.regularHoursWage = 1000L;
        dto2.sundayHoursWage = 1500L;
        dto2.bonusHoursWage = 2000L;

        list.add(dto);
        list.add(dto2);

        return list;
    }
}
