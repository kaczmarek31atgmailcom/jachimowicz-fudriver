package com.fungisearch.fudriver.timeRecorder.command.model;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.repository.PersonRepository;
import com.fungisearch.fudriver.timeRecorder.command.repository.TimeWorkLogRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimeWorkLogFactoryTest {

    @Mock
    private TimeWorkLogRepository timeWorkLogRepository;
    @Mock
    private BeanValidator beanValidator;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private TimeWorkLogFactory timeWorkLogFactory;

    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    Person person = new Person();

    @Before
    public void setUp() {
        person.id = 1L;
    }

    @Test
    public void shouldReturnTwoDaysCollection() {
        //Given
        when(timeWorkLogRepository.findForPeriod(any(Date.class), any(Date.class), any(Person.class))).thenReturn(getTwoDays());
        //When
        Map<Date, TimeWorkLogDay> tested = timeWorkLogFactory.getMonthDaysSummarized(new Date(), person);
        //Then
        assertEquals(2, tested.size());
    }

    @Test
    public void shouldSetStartDays() {
        //Given
        when(timeWorkLogRepository.findForPeriod(any(Date.class), any(Date.class), any(Person.class))).thenReturn(getTwoDays());
        //When
        Map<Date, TimeWorkLogDay> map = timeWorkLogFactory.getMonthDaysSummarized(new Date(), person);
        TimeWorkLogDay day1 = map.get(DateUtils.getStartOfDay(LocalDateTime.parse("2017-02-03 12:00:00", formatter).toDate()));
        TimeWorkLogDay day2 = map.get(DateUtils.getStartOfDay(LocalDateTime.parse("2017-02-04 12:00:00", formatter).toDate()));
        //Then
        assertEquals(LocalDateTime.parse("2017-02-03 12:00:00", formatter).toDate(), day1.startTime);
        assertEquals(LocalDateTime.parse("2017-02-04 12:00:00", formatter).toDate(), day2.startTime);
    }

    @Test
    public void shouldSetEndDays() {
        //Given
        when(timeWorkLogRepository.findForPeriod(any(Date.class), any(Date.class), any(Person.class))).thenReturn(getTwoDays());
        //When
        Map<Date, TimeWorkLogDay> map = timeWorkLogFactory.getMonthDaysSummarized(new Date(), person);
        TimeWorkLogDay day1 = map.get(DateUtils.getStartOfDay(LocalDateTime.parse("2017-02-03 12:00:00", formatter).toDate()));
        TimeWorkLogDay day2 = map.get(DateUtils.getStartOfDay(LocalDateTime.parse("2017-02-04 12:00:00", formatter).toDate()));
        //Then
        assertEquals(LocalDateTime.parse("2017-02-03 13:00:00", formatter).toDate(), day1.endTime);
        assertEquals(LocalDateTime.parse("2017-02-04 14:00:00", formatter).toDate(), day2.endTime);
    }

    @Test
    public void shouldCountWorkMinutes() {
        //Given
        when(timeWorkLogRepository.findForPeriod(any(Date.class), any(Date.class), any(Person.class))).thenReturn(getTwoDays());
        //When
        Map<Date, TimeWorkLogDay> map = timeWorkLogFactory.getMonthDaysSummarized(new Date(), person);
        TimeWorkLogDay day1 = map.get(DateUtils.getStartOfDay(LocalDateTime.parse("2017-02-03 12:00:00", formatter).toDate()));
        TimeWorkLogDay day2 = map.get(DateUtils.getStartOfDay(LocalDateTime.parse("2017-02-04 12:00:00", formatter).toDate()));
        //Then
        assertEquals(60L, day1.workMinutes);
        assertEquals(120L, day2.workMinutes);
    }

    @Test
    public void shouldCountTotalMinutes() {
        //Given
        when(timeWorkLogRepository.findForPeriod(any(Date.class), any(Date.class), any(Person.class))).thenReturn(getTwoDays());
        //When
        Map<Date, TimeWorkLogDay> map = timeWorkLogFactory.getMonthDaysSummarized(new Date(), person);
        TimeWorkLogDay day1 = map.get(DateUtils.getStartOfDay(LocalDateTime.parse("2017-02-03 12:00:00", formatter).toDate()));
        TimeWorkLogDay day2 = map.get(DateUtils.getStartOfDay(LocalDateTime.parse("2017-02-04 12:00:00", formatter).toDate()));
        //Then
        assertEquals(60L, day1.totalMinutes);
        assertEquals(120L, day2.totalMinutes);
    }

    @Test
    public void shouldCountPauseTime(){
        //Given
        when(timeWorkLogRepository.findForPeriod(any(Date.class), any(Date.class), any(Person.class))).thenReturn(getTwoPeriodsOneDay());
        //When
        Map<Date, TimeWorkLogDay> map = timeWorkLogFactory.getMonthDaysSummarized(new Date(), person);
        TimeWorkLogDay day = map.get(DateUtils.getStartOfDay(LocalDateTime.parse("2017-02-03 12:00:00", formatter).toDate()));
        //Then
        assertEquals(120L,day.pauseMinutes);
    }

    @Test
    public void shouldCountTotalTimeForTwoPeriodsInSingleDay(){
        //Given
        when(timeWorkLogRepository.findForPeriod(any(Date.class), any(Date.class), any(Person.class))).thenReturn(getTwoPeriodsOneDay());
        //When
        Map<Date, TimeWorkLogDay> map = timeWorkLogFactory.getMonthDaysSummarized(new Date(), person);
        TimeWorkLogDay day = map.get(DateUtils.getStartOfDay(LocalDateTime.parse("2017-02-03 12:00:00", formatter).toDate()));
        //Then
        assertEquals(360L,day.totalMinutes);
    }


    private List<TimeWorkLog> getTwoDays() {

        TimeWorkLog timeWorkLog1 = new TimeWorkLog(timeWorkLogRepository, applicationEventPublisher, beanValidator);
        timeWorkLog1.setStartTime(LocalDateTime.parse("2017-02-03 12:00:00", formatter).toDate());
        timeWorkLog1.setEndTime(LocalDateTime.parse("2017-02-03 13:00:00", formatter).toDate());
        timeWorkLog1.setPerson(person);
        timeWorkLog1.setOpened(false);

        TimeWorkLog timeWorkLog2 = new TimeWorkLog(timeWorkLogRepository, applicationEventPublisher, beanValidator);
        timeWorkLog2.setStartTime(LocalDateTime.parse("2017-02-04 12:00:00", formatter).toDate());
        timeWorkLog2.setEndTime(LocalDateTime.parse("2017-02-04 14:00:00", formatter).toDate());
        timeWorkLog2.setPerson(person);
        timeWorkLog2.setOpened(false);

        List<TimeWorkLog> result = new ArrayList<>();
        result.add(timeWorkLog1);
        result.add(timeWorkLog2);
        return result;
    }

    private List<TimeWorkLog> getTwoPeriodsOneDay() {

        TimeWorkLog timeWorkLog1 = new TimeWorkLog(timeWorkLogRepository, applicationEventPublisher, beanValidator);
        timeWorkLog1.setStartTime(LocalDateTime.parse("2017-02-03 08:00:00", formatter).toDate());
        timeWorkLog1.setEndTime(LocalDateTime.parse("2017-02-03 10:00:00", formatter).toDate());
        timeWorkLog1.setPerson(person);
        timeWorkLog1.setOpened(false);

        TimeWorkLog timeWorkLog2 = new TimeWorkLog(timeWorkLogRepository, applicationEventPublisher, beanValidator);
        timeWorkLog2.setStartTime(LocalDateTime.parse("2017-02-03 12:00:00", formatter).toDate());
        timeWorkLog2.setEndTime(LocalDateTime.parse("2017-02-03 14:00:00", formatter).toDate());
        timeWorkLog2.setPerson(person);
        timeWorkLog2.setOpened(false);

        List<TimeWorkLog> result = new ArrayList<>();
        result.add(timeWorkLog1);
        result.add(timeWorkLog2);
        return result;
    }

}