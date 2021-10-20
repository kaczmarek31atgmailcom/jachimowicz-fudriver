package com.fungisearch.fudriver.timeRecorder.command;

import com.fungisearch.fudriver.person.group.command.model.PersonGroup;
import com.fungisearch.fudriver.person.group.command.repository.PersonGroupRepository;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.repository.PersonRepository;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLog;
import com.fungisearch.fudriver.timeRecorder.command.repository.TimeWorkLogRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.*;

@ContextConfiguration(locations = {"/test-spring.xml"})
public class RegisterTimeWorkCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private BeanValidator beanValidator;

    @Autowired
    private TimeWorkLogRepository timeWorkLogRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonGroupRepository personGroupRepository;

    @Autowired
    private CloseTimeWorkCommandHandler registerTimeWorkCommandHandler;

    private TimeWorkLog timeWorkLog;
    private Person person;


    @Before
    public void setUp() {
        PersonGroup personGroup = new PersonGroup.PersonGroupBuilder(personGroupRepository, beanValidator).name("grupa_testowa").build();
        personGroup.create();
        person = new Person.PersonBuilder(personRepository, beanValidator)
                .active(true)
                .nr(1L)
                .groupId(personGroup.getId())
                .build();
        person.create();
        timeWorkLog = new TimeWorkLog.TimeWorkLogBuilder(timeWorkLogRepository, applicationEventPublisher, beanValidator)
                .person(person)
                .build();
        timeWorkLog.register();
    }

    @Test
    public void shouldCloseOpenedTimeWork() {
        //Given
        assertTrue(timeWorkLog.getOpened());
        assertNotNull(timeWorkLog.getStartTime());
        assertNull(timeWorkLog.getEndTime());
        //When
        registerTimeWorkCommandHandler.handle(timeWorkLog.getId());
        //Then
        assertFalse(timeWorkLog.getOpened());
        assertNotNull(timeWorkLog.getStartTime());
        assertNotNull(timeWorkLog.getEndTime());
    }

    @Test
    public void shouldNotCloseAlreadyClosedTimeWorkLog() {
        //Given
        assertTrue(timeWorkLog.getOpened());
        timeWorkLog.close();
        assertFalse(timeWorkLog.getOpened());
        //When & Then
        try {
            timeWorkLog.close();
            fail();
        } catch (IllegalStateException ex) {
        }
    }
}