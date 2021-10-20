package com.fungisearch.fudriver.payroll.wage.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.payroll.wage.command.model.WageFactory;
import com.fungisearch.fudriver.payroll.wage.command.model.WageHeader;
import com.fungisearch.fudriver.payroll.wage.command.repository.WageRepository;
import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.person.person.command.repository.PersonRepository;
import com.fungisearch.fudriver.type.command.repository.TypeRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateEmployeeAccordHeaderCommandHandlerTest {

    @Mock
    private PersonFactory personFactory;
    @Mock
    private WageFactory wageFactory;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private BeanValidator beanValidator;
    @Mock
    private WageRepository wageRepository;
    @Mock
    private TypeRepository typeRepository;

    @InjectMocks
    private UpdateEmployeeAccordHeaderCommandHandler handler;

    @Test
    public void shouldChangeHeader(){
        //Given
        WageHeader header0 = createWageHeader("header0");
        WageHeader header1 = createWageHeader("header1");
        when(wageFactory.findHeader(any(Long.class))).thenReturn(header1);
        Person person = createPerson(PayrollTypeEnum.ACCORD);
        person.setWageHeader(header0);
        when(personFactory.find(any(Long.class))).thenReturn(person);
        UpdateEmployeeAccordHeaderCommand command = new UpdateEmployeeAccordHeaderCommand();
        command.personId = 1L;
        command.headerId = 1L;
        //When
        handler.handle(command);
        //Then
        assertEquals(header1,person.getWageHeader());
    }

    @Test
    public void shouldReturnValieCommandResult(){
        //Given
        WageHeader header0 = createWageHeader("header0");
        WageHeader header1 = createWageHeader("header1");
        when(wageFactory.findHeader(any(Long.class))).thenReturn(header1);
        Person person = createPerson(PayrollTypeEnum.ACCORD);
        person.setWageHeader(header0);
        when(personFactory.find(any(Long.class))).thenReturn(person);
        UpdateEmployeeAccordHeaderCommand command = new UpdateEmployeeAccordHeaderCommand();
        command.personId = 1L;
        command.headerId = 1L;
        //When
        CommandResult tested = handler.handle(command);
        //Then
        assertEquals(CommandResult.Status.OK,tested.status);
        assertEquals(new Long(1), tested.entityId);
        assertEquals("EmployeeAccordHeaderUpdated",tested.body);
    }


    private Person createPerson(PayrollTypeEnum payrollType) {
        Person person = new Person.PersonBuilder(personRepository, beanValidator)
                .payrollType(payrollType)
                .id(1L)
                .nr(1L)
                .name("test_name")
                .surname("test_surname")
                .groupId(1L)
                .active(true)
                .build();
        person.create();
        return person;
    }

    public WageHeader createWageHeader(String name) {
        return new WageHeader.WageHeaderBuilder(wageRepository, beanValidator, typeRepository)
                .name(name)
                .build();
    }
}
