package com.fungisearch.fudriver.timeRecorder.query.dao;

import com.fungisearch.fudriver.person.group.command.model.PersonGroup;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.testTools.CreatePerson;
import com.fungisearch.fudriver.testTools.CreateTimeWorkLog;
import com.fungisearch.fudriver.testTools.FlushDao;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLog;
import com.fungisearch.fudriver.timeRecorder.query.dto.PersonWorkTimeDto;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by marcin on 15.01.17.
 */
@ContextConfiguration(locations = {"/test-spring.xml"})
public class WorkTimeLogDaoITCase extends AbstractTransactionalJUnit4SpringContextTests {


    @Autowired
    private  WorkTimeLogDao workTimeLogDao;
    @Autowired
    private FlushDao flushDao;

    @Autowired
    private CreatePerson createPerson;
    @Autowired
    private CreateTimeWorkLog createTimeWorkLog;

    private Person person;
    private Date startTime;
    private Date endTime;
    private SimpleDateFormat hourSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private TimeWorkLog timeWorkLog;
    private PersonGroup personGroup;


    @Before
    public void setUp() throws Exception{
        person = createPerson.create();
        startTime = hourSdf.parse("2017-01-04 08:00:00");
        endTime = hourSdf.parse("2017-01-04 16:00:00");
        timeWorkLog = createTimeWorkLog.create(startTime, endTime,person.getId());
        flushDao.flush();
    }


    @Test
    public void shouldExecuteGetPersonWorkTime() throws Exception{
        //Given
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse("2017-01-01");
        Date endDate = sdf.parse("2017-01-20");
        //When
        List<PersonWorkTimeDto> tested = workTimeLogDao.getPersonWorkTime(startTime, endTime);
        //Then
        Assert.assertEquals(1,tested.size());
        Assert.assertEquals(person.getId(), (Long) tested.get(0).personId);
        Assert.assertEquals(DateUtils.truncate(startTime, Calendar.SECOND), DateUtils.truncate(tested.get(0).startDate, Calendar.SECOND));
        Assert.assertEquals(DateUtils.truncate(endTime, Calendar.SECOND), DateUtils.truncate(tested.get(0).endDate, Calendar.SECOND));
    }
}