package com.fungisearch.fudriver.payroll.wage.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.person.person.command.repository.PersonRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateEmployeeHourlyRegularWageCommandHandlerTest {

    @Mock
    private PersonFactory personFactory;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private BeanValidator beanValidator;
    @InjectMocks
    private UpdateEmployeeHourlyRegularWageCommandHandler handler;

    @Test
    public void shouldUpdateRegularWage(){
        //Given
        Person person = createPerson();
        when(personFactory.find(any(Long.class))).thenReturn(person);
        UpdateEmployeeHourlyWageCommand command = new UpdateEmployeeHourlyWageCommand();
        command.personId = 1L;
        command.value = 100L;
        //When
        handler.handle(command);
        //Then
        assertEquals(new Long(100),person.getRegularWage());
    }

    @Test
    public void shouldCreateValidCommandResult(){
        //Given
        Person person = createPerson();
        when(personFactory.find(any(Long.class))).thenReturn(person);
        UpdateEmployeeHourlyWageCommand command = new UpdateEmployeeHourlyWageCommand();
        command.personId = 1L;
        command.value = 100L;
        //When
        CommandResult tested = handler.handle(command);
        //Then
        assertEquals(new Long(1), tested.entityId);
        assertEquals(CommandResult.Status.OK,tested.status);
        assertEquals("RegularWageUpdated",tested.body);
    }

    private Person createPerson(){
        return new Person.PersonBuilder(personRepository,beanValidator)
                .groupId(1L)
                .id(1L)
                .name("test_person")
                .surname("test_person")
                .nr(1L)
                .build();
    }
}