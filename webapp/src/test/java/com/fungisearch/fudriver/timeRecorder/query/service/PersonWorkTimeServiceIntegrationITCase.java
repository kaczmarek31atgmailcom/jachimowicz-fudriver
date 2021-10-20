package com.fungisearch.fudriver.timeRecorder.query.service;

import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.testTools.CreatePerson;
import com.fungisearch.fudriver.testTools.CreateTimeWorkLog;
import com.fungisearch.fudriver.testTools.FlushDao;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLog;
import com.fungisearch.fudriver.timeRecorder.query.dto.PersonDailyWorkTimeDto;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by marcin on 15.01.17.
 */
@ContextConfiguration(locations = {"/test-spring.xml"})
public class PersonWorkTimeServiceIntegrationITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CreatePerson createPerson;
    @Autowired
    private CreateTimeWorkLog createTimeWorkLog;
    @Autowired
    private PersonWorkTimeService personWorkTimeService;
    @Autowired
    private FlushDao flushDao;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdfShort = new SimpleDateFormat("yyyy-MM-dd");
    private Person person;

    @Test
    public void shouldCreatePerson() {
        person = createPerson.create();
        assertTrue(true);
    }

    @Test
    public void shouldReturnWorkMinutesForOneClosedPeriod() throws Exception {
        //Given
        Date startTime = sdf.parse("2017-01-01 08:00:00");
        Date endTime = sdf.parse("2017-01-01 16:00:00");
        Date reportStartDate = sdfShort.parse("2016-12-15");
        Date reportEndDate = sdfShort.parse("2017-01-15");
        Person person = createPerson.create();
        TimeWorkLog timeWorkLog = createTimeWorkLog.create(startTime, endTime, person.getId());
        flushDao.flush();
        //When
        List<PersonDailyWorkTimeDto> tested = personWorkTimeService.getDailyWorkMinutes(reportStartDate, reportEndDate);
        //Then
        assertEquals(1, tested.size());
        assertEquals(timeWorkLog.getPerson().getId(), tested.get(0).personId);
        assertEquals(DateUtils.truncate(sdfShort.parse("2017-01-01"), Calendar.SECOND), DateUtils.truncate(tested.get(0).day, Calendar.SECOND));
        assertEquals(new Long(480), tested.get(0).workMinutes);
    }

    @Test
    public void shouldReturnWorkMinutesForTwoDaysClosedPeriod() throws Exception {
        //Given
        Date startTime = sdf.parse("2017-01-01 08:00:00");
        Date endTime = sdf.parse("2017-01-02 16:00:00");
        Date reportStartDate = sdfShort.parse("2016-12-15");
        Date reportEndDate = sdfShort.parse("2017-01-15");
        Person person = createPerson.create();
        TimeWorkLog timeWorkLog = createTimeWorkLog.create(startTime, endTime, person.getId());
        flushDao.flush();
        //When
        List<PersonDailyWorkTimeDto> tested = personWorkTimeService.getDailyWorkMinutes(reportStartDate, reportEndDate);
        //Then
        assertEquals(2, tested.size());
        assertEquals(timeWorkLog.getPerson().getId(), tested.get(0).personId);
        assertEquals(DateUtils.truncate(sdfShort.parse("2017-01-01"), Calendar.SECOND), DateUtils.truncate(tested.get(0).day, Calendar.SECOND));
        assertEquals(new Long(960), tested.get(0).workMinutes);
        assertEquals(DateUtils.truncate(sdfShort.parse("2017-01-02"), Calendar.SECOND), DateUtils.truncate(tested.get(1).day, Calendar.SECOND));
        assertEquals(new Long(960), tested.get(1).workMinutes);

    }

    @Test
    public void shouldReturnWorkMinutesForOpenedPeriod() throws Exception {
        //Given
        Date startTime = sdf.parse("2017-01-01 08:00:00");
        Date endTime = null;
        Date reportStartDate = sdfShort.parse("2016-12-15");
        Date reportEndDate = sdfShort.parse("2017-01-15");
        Person person = createPerson.create();
        TimeWorkLog timeWorkLog = createTimeWorkLog.create(startTime, endTime, person.getId());
        flushDao.flush();
        //When
        List<PersonDailyWorkTimeDto> tested = personWorkTimeService.getDailyWorkMinutes(reportStartDate, reportEndDate);
        //Then
        assertEquals(tested.size(),15);
        assertEquals(tested.get(0).day, sdfShort.parse("2017-01-01"));
        assertEquals(tested.get(0).workMinutes,new Long(960));
        assertEquals(tested.get(1).day, sdfShort.parse("2017-01-02"));
        assertEquals(tested.get(1).workMinutes,new Long(1440));
        assertEquals(tested.get(2).day, sdfShort.parse("2017-01-03"));
        assertEquals(tested.get(2).workMinutes,new Long(1440));
        assertEquals(tested.get(3).day, sdfShort.parse("2017-01-04"));
        assertEquals(tested.get(3).workMinutes,new Long(1440));
        assertEquals(tested.get(4).day, sdfShort.parse("2017-01-05"));
        assertEquals(tested.get(4).workMinutes,new Long(1440));
        assertEquals(tested.get(5).day, sdfShort.parse("2017-01-06"));
        assertEquals(tested.get(5).workMinutes,new Long(1440));
        assertEquals(tested.get(6).day, sdfShort.parse("2017-01-07"));
        assertEquals(tested.get(6).workMinutes,new Long(1440));

        assertEquals(tested.get(14).day, sdfShort.parse("2017-01-15"));
        assertEquals(tested.get(14).workMinutes,new Long(1440));
    }
}