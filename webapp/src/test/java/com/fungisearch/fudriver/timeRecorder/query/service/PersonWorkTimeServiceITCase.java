package com.fungisearch.fudriver.timeRecorder.query.service;

import com.fungisearch.fudriver.person.person.query.dao.PersonDao;
import com.fungisearch.fudriver.person.person.query.dto.PersonHeaderDto;
import com.fungisearch.fudriver.timeRecorder.query.dao.WorkTimeLogDao;
import com.fungisearch.fudriver.timeRecorder.query.dto.PersonDailyWorkTimeDto;
import com.fungisearch.fudriver.timeRecorder.query.dto.PersonWorkTimeDto;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.when;

/**
 * Created by marcin on 13.01.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class PersonWorkTimeServiceITCase {


    @Mock
    private WorkTimeLogDao workTimeLogDao;

    @Mock
    private PersonDao personDao;

    //@Autowired
    private PersonWorkTimeService personWorkTimeService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Date reportStartDate;
    private Date reportEndDate;
    private List<PersonWorkTimeDto> workTimeList;

    @Before
    public void setUp() throws Exception {
        personWorkTimeService = new PersonWorkTimeService(workTimeLogDao,personDao);
        reportStartDate = sdf.parse("2017-01-01 00:00:00");
        reportEndDate = sdf.parse("2017-01-10 23:59:59");
        PersonWorkTimeDto p1 = new PersonWorkTimeDto();
        p1.personId = 1001;
        p1.startDate = sdf.parse("2017-01-01 08:00:00");
        p1.endDate = sdf.parse("2017-01-01 10:00:00");

        PersonWorkTimeDto p2 = new PersonWorkTimeDto();
        p2.personId = 1002;
        p2.startDate = sdf.parse("2017-01-01 08:00:00");
        p2.endDate = null;

        workTimeList = new ArrayList<>();
        workTimeList.add(p1);

        when(workTimeLogDao.getPersonWorkTime(any(Date.class),any(Date.class))).thenReturn(workTimeList);

        List<PersonHeaderDto> personHeaders = new ArrayList<>();
        PersonHeaderDto marcin = new PersonHeaderDto();
        marcin.id = 1001L;
        marcin.name = "Marcin";
        marcin.surname = "Kaczmarek";
        marcin.nr = 1L;

        PersonHeaderDto warren = new PersonHeaderDto();
        warren.id = 1002L;
        warren.name = "Warren";
        warren.surname = "Buffet";
        warren.nr = 2L;

        personHeaders.add(marcin);
        personHeaders.add(warren);
        when(personDao.getAllHeaders()).thenReturn(personHeaders);
    }

    @Test
    public void shouldReturnListOfPeriods() throws Exception {
        //Given
        PersonWorkTimeDto p1 = new PersonWorkTimeDto();
        p1.personId = 1001;
        p1.startDate = sdf.parse("2017-01-01 08:00:00");
        p1.endDate = sdf.parse("2017-01-01 10:00:00");
        //When
        List<PersonWorkTimeDto> tested = personWorkTimeService.splitToDays(p1,reportEndDate);
        //Then
        Assert.assertEquals(tested.size(), 1);
        Assert.assertEquals(p1.personId, tested.get(0).personId);
        Assert.assertEquals(DateUtils.truncate(p1.startDate, Calendar.SECOND), DateUtils.truncate(tested.get(0).startDate, Calendar.SECOND));
        Assert.assertEquals(DateUtils.truncate(p1.endDate, Calendar.SECOND), DateUtils.truncate(tested.get(0).endDate, Calendar.SECOND));
    }


    @Test
    public void shouldCreateToPeriodsIfStartDayAnotherThanEndDay() throws Exception {
        //Given
        PersonWorkTimeDto p1 = new PersonWorkTimeDto();
        p1.personId = 1001;
        p1.startDate = sdf.parse("2017-01-01 08:00:00");
        p1.endDate = null;
        //When
        List<PersonWorkTimeDto> tested = personWorkTimeService.splitToDays(p1,reportEndDate);
        //Then
        Assert.assertTrue(tested.size() > 1);
    }

    @Test
    public void shouldSplitTo3Days() throws Exception {
        //Given
        PersonWorkTimeDto p1 = new PersonWorkTimeDto();
        p1.personId = 1001;
        p1.startDate = sdf.parse("2017-01-01 08:00:00");
        p1.endDate = sdf.parse("2017-01-03 12:00:00");
        //When
        List<PersonWorkTimeDto> tested = personWorkTimeService.splitToDays(p1,reportEndDate);
        //Then
        Assert.assertEquals(DateUtils.truncate(p1.startDate, Calendar.SECOND), DateUtils.truncate(tested.get(0).startDate, Calendar.SECOND));
        Assert.assertEquals(DateUtils.truncate(sdf.parse("2017-01-01 23:59:59"), Calendar.SECOND), DateUtils.truncate(tested.get(0).endDate, Calendar.SECOND));
        Assert.assertEquals(p1.personId, tested.get(0).personId);
        Assert.assertEquals(DateUtils.truncate(sdf.parse("2017-01-02 00:00:00"), Calendar.SECOND), DateUtils.truncate(tested.get(1).startDate, Calendar.SECOND));
        Assert.assertEquals(DateUtils.truncate(sdf.parse("2017-01-02 23:59:59"), Calendar.SECOND), DateUtils.truncate(tested.get(1).endDate, Calendar.SECOND));
        Assert.assertEquals(p1.personId, tested.get(1).personId);
        Assert.assertEquals(DateUtils.truncate(sdf.parse("2017-01-03 00:00:00"), Calendar.SECOND), DateUtils.truncate(tested.get(2).startDate, Calendar.SECOND));
        Assert.assertEquals(DateUtils.truncate(p1.endDate, Calendar.SECOND), DateUtils.truncate(tested.get(2).endDate, Calendar.SECOND));
        Assert.assertEquals(p1.personId, tested.get(2).personId);
    }


    @Test
    public void shouldReturnAlreadyExistingPersonDailyWorkTimeDto() throws Exception{
        //Given
        List<PersonDailyWorkTimeDto> list = new ArrayList<>();
        PersonDailyWorkTimeDto personDailyWorkTimeDto = new PersonDailyWorkTimeDto();
        personDailyWorkTimeDto.personId = 1L;
        personDailyWorkTimeDto.day = sdf.parse("2017-01-12 00:00:00");
        list.add(personDailyWorkTimeDto);
        //When
        PersonDailyWorkTimeDto tested = personWorkTimeService.findPerson(personDailyWorkTimeDto.personId,personDailyWorkTimeDto.day,list);
        //Then
        Assert.assertTrue(true);
        Assert.assertEquals(personDailyWorkTimeDto.personId,tested.personId);
        Assert.assertEquals(personDailyWorkTimeDto.day,tested.day);
    }
    @Test
    public void shouldReturnNullPersonDailyWorkTimeDto() throws Exception{
        //Given
        List<PersonDailyWorkTimeDto> list = new ArrayList<>();
        PersonDailyWorkTimeDto personDailyWorkTimeDto = new PersonDailyWorkTimeDto();
        personDailyWorkTimeDto.personId = 1L;
        personDailyWorkTimeDto.day = sdf.parse("2017-01-12 00:00:00");
        list.add(personDailyWorkTimeDto);
        Long testedPersonId = 2L;
        Date testedDay = personDailyWorkTimeDto.day = sdf.parse("2017-01-11 00:00:00");
        //When
        PersonDailyWorkTimeDto tested = personWorkTimeService.findPerson(testedPersonId,testedDay,list);
        //Then
        Assert.assertNull(tested);
    }


    @Test
    public void shouldFindMinutesBetweenDate() throws Exception{
        //Given
        Date startDate = sdf.parse("2017-01-12 00:00:00");
        Date endDate = sdf.parse("2017-01-12 00:01:00");
        //When
        Long tested = personWorkTimeService.minutesBetween(startDate,endDate);
        //Then
        Assert.assertEquals(new Long(1),tested);
    }

    @Test
    public void shouldReturnSinglePersonDailyWorkTimeDto() {
        //Given
        //When
        List<PersonDailyWorkTimeDto> result = personWorkTimeService.getDailyWorkMinutes(reportStartDate, reportEndDate);
        //Then
        Assert.assertEquals(new Long(120),result.get(0).workMinutes);
    }

    @Test
    public void shouldFindTwoPeriodsSinglePersonDailyWorkTimeDto() throws Exception{
        //Given
        PersonWorkTimeDto p3 = new PersonWorkTimeDto();
        p3.personId = 1001;
        p3.startDate = sdf.parse("2017-01-01 11:00:00");
        p3.endDate = sdf.parse("2017-01-01 11:10:00");
        workTimeList.add(p3);
        //When
        List<PersonDailyWorkTimeDto> result = personWorkTimeService.getDailyWorkMinutes(reportStartDate, reportEndDate);
        //Then
        Assert.assertEquals(new Long(130),result.get(0).workMinutes);
    }

    @Test
    public void shouldFindPersonById(){
        //Given
        //Then
        PersonHeaderDto tested = personWorkTimeService.findPersonById(1001L);
        //Then
        Assert.assertEquals(tested.id,new Long(1001));
        Assert.assertEquals(tested.nr,new Long(1));
        Assert.assertEquals(tested.name, new String("Marcin"));
        Assert.assertEquals(tested.surname, new String("Kaczmarek"));
    }

    @Test
    public void shouldFindTwoPeriodsSinglePersonDailyWorkTimeDtoWithNameAndSurname() throws Exception{
        //Given
        PersonWorkTimeDto p3 = new PersonWorkTimeDto();
        p3.personId = 1001;
        p3.startDate = sdf.parse("2017-01-01 11:00:00");
        p3.endDate = sdf.parse("2017-01-01 11:11:00");
        workTimeList.add(p3);
        //When
        List<PersonDailyWorkTimeDto> tested = personWorkTimeService.getDailyWorkMinutes(reportStartDate, reportEndDate);
        //Then
        Assert.assertEquals(new Long(131),tested.get(0).workMinutes);
        Assert.assertEquals(new Long(1001),tested.get(0).personId);
        Assert.assertEquals(new Long(1), tested.get(0).nr);
        Assert.assertEquals(new String("Marcin"), tested.get(0).name);
        Assert.assertEquals(new String("Kaczmarek"), tested.get(0).surname);
    }


    @Test
    public void shouldGetEndDayOfLastPeriod() throws Exception{
        //Given
        List<PersonWorkTimeDto> list = new ArrayList<>();
        PersonWorkTimeDto dto = new PersonWorkTimeDto();
        Date today = new Date();
        Calendar calStartDate = Calendar.getInstance();
        calStartDate.setTime(today);
        dto.personId = 1010;
        calStartDate.add(Calendar.DATE, -10);
        dto.startDate = calStartDate.getTime();
        calStartDate.setTime(today);
        calStartDate.add(Calendar.DATE, 10);
        list.add(dto);

        Calendar calReportEndDate = Calendar.getInstance();
        calReportEndDate.setTime(today);
        calReportEndDate.add(Calendar.DATE, 10);
        Date reportEndDate = calReportEndDate.getTime();
        //When
        List<PersonWorkTimeDto> tested = personWorkTimeService.setEndDateOfLastPeriod(list,reportEndDate);
        //Then
        Assert.assertEquals(1,tested.size());
        Assert.assertEquals(DateUtils.truncate(tested.get(0).endDate, Calendar.SECOND), DateUtils.truncate(today, Calendar.SECOND));
    }

    @Test
    public void shouldGetEndDayOfLastPeriodForTwoPeriods() throws Exception{
        //Given
        List<PersonWorkTimeDto> list = new ArrayList<>();
        PersonWorkTimeDto dto1 = new PersonWorkTimeDto();
        PersonWorkTimeDto dto2 = new PersonWorkTimeDto();
        Date today = new Date();
        Calendar calStartDatePeriod1 = Calendar.getInstance();
        calStartDatePeriod1.setTime(today);
        calStartDatePeriod1.add(Calendar.DATE, -10);
        Calendar calEndDatePeriod1 = Calendar.getInstance();
        calEndDatePeriod1.setTime(today);
        calEndDatePeriod1.add(Calendar.DATE, -9);
        dto1.personId = 1010;
        dto1.startDate = calStartDatePeriod1.getTime();
        dto1.endDate = calEndDatePeriod1.getTime();
        Calendar calStartPeriod2 = Calendar.getInstance();
        calStartPeriod2.setTime(today);
        calStartPeriod2.add(Calendar.DATE,-8);
        dto2.personId = 1010;
        dto2.startDate = calStartPeriod2.getTime();

        list.add(dto1);
        list.add(dto2);

        Calendar calReportEndDate = Calendar.getInstance();
        calReportEndDate.setTime(today);
        calReportEndDate.add(Calendar.DATE, 10);
        Date reportEndDate = calReportEndDate.getTime();
        //When
        List<PersonWorkTimeDto> tested = personWorkTimeService.setEndDateOfLastPeriod(list,reportEndDate);
        //Then
        Assert.assertEquals(2,tested.size());
        Assert.assertEquals(DateUtils.truncate(calEndDatePeriod1.getTime(), Calendar.SECOND),(DateUtils.truncate(tested.get(0).endDate, Calendar.SECOND)));
        Assert.assertEquals(DateUtils.truncate(today, Calendar.SECOND),(DateUtils.truncate(tested.get(1).endDate, Calendar.SECOND)));
    }

    @Test
    public void shouldHandleOneElementListWithNoEndDate(){
        //Given
        List<PersonWorkTimeDto> list = new ArrayList<>();
        PersonWorkTimeDto dto1 = new PersonWorkTimeDto();
        Date today = new Date();
        Calendar calStartDatePeriod1 = Calendar.getInstance();
        calStartDatePeriod1.setTime(today);
        calStartDatePeriod1.add(Calendar.DATE, -10);
        dto1.personId = 1010;
        dto1.startDate = calStartDatePeriod1.getTime();
        list.add(dto1);

        Calendar calReportEndDate = Calendar.getInstance();
        calReportEndDate.setTime(today);
        calReportEndDate.add(Calendar.DATE, 10);
        Date reportEndDate = calReportEndDate.getTime();
        //When
        List<PersonWorkTimeDto> tested = personWorkTimeService.setEndDateOfLastPeriod(list,reportEndDate);
        //Then
        Assert.assertEquals(1,tested.size());
        Assert.assertEquals(DateUtils.truncate(today, Calendar.SECOND),(DateUtils.truncate(tested.get(0).endDate, Calendar.SECOND)));
    }

    @Test
    public void shouldSetReportEndDateIfBelowCurrentDate(){
        //Given
        List<PersonWorkTimeDto> list = new ArrayList<>();
        PersonWorkTimeDto dto1 = new PersonWorkTimeDto();
        Date today = new Date();
        Calendar calStartDatePeriod1 = Calendar.getInstance();
        calStartDatePeriod1.setTime(today);
        calStartDatePeriod1.add(Calendar.DATE, -10);
        Calendar calEndPeriod1 = Calendar.getInstance();
        calEndPeriod1.setTime(today);
        calEndPeriod1.add(Calendar.DATE, -4);

        dto1.personId = 1010;
        dto1.startDate = calStartDatePeriod1.getTime();
        list.add(dto1);

        Calendar calReportEndDate = Calendar.getInstance();
        calReportEndDate.setTime(today);
        calReportEndDate.add(Calendar.DATE, -6);
        Date reportEndDate = calReportEndDate.getTime();
        //When
        List<PersonWorkTimeDto> tested = personWorkTimeService.setEndDateOfLastPeriod(list,reportEndDate);
        //Then
        Assert.assertEquals(1,tested.size());
    }

}