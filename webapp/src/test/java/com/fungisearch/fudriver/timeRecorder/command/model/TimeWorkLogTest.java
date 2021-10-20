package com.fungisearch.fudriver.timeRecorder.command.model;

import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.repository.PersonRepository;
import com.fungisearch.fudriver.timeRecorder.command.repository.TimeWorkLogRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

@RunWith(MockitoJUnitRunner.class)
public class TimeWorkLogTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private BeanValidator beanValidator;
    @Mock
    private TimeWorkLogRepository timeWorkLogRepository;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;


    @Test
    public void shouldCloseForTheSameDay(){
        //Given
        DateTimeFormatter ft = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        DateTime startDate = ft.parseDateTime("2017-03-01 13:00");
        DateTime startDate1 = ft.parseDateTime("2017-03-01 10:00");

        Person person = new Person.PersonBuilder(personRepository,beanValidator).id(1L).build();
        TimeWorkLog timeWorkLog = new TimeWorkLog.TimeWorkLogBuilder(timeWorkLogRepository,applicationEventPublisher,beanValidator)
                .person(person)
                .startTime(startDate.toDate())
                .build();
        timeWorkLog.edit(new TimeWorkLog.Edit().startDate(startDate1.toDate()));
        //When
        timeWorkLog.close();
        //Then
        Assert.assertFalse(timeWorkLog.getOpened());
    }
}