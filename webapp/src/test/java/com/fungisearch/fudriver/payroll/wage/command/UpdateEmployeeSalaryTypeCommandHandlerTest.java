package com.fungisearch.fudriver.payroll.wage.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.person.person.command.repository.PersonRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateEmployeeSalaryTypeCommandHandlerTest {

    @Mock
    private PersonFactory personFactory;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private BeanValidator beanValidator;

    @InjectMocks
    private UpdateEmployeeSalaryTypeCommandHandler handler;


    @Test
    public void shouldSetHourlyPayrollType() {
       //Given
        Person tested = createPerson(PayrollTypeEnum.ACCORD);
        when(personFactory.find(any(Long.class))).thenReturn(tested);
        UpdateEmployeeSalaryTypeCommand command = new UpdateEmployeeSalaryTypeCommand();
        command.personId = tested.getId();
        command.payrollType = PayrollTypeEnum.HOURLY;
        //Then
        handler.handle(command);
        //Then
        assertEquals(PayrollTypeEnum.HOURLY, tested.getPayrollType());
    }


    @Test
    public void shouldSetAccordPayrollType() {
        //Given
        Person tested = createPerson(PayrollTypeEnum.HOURLY);
        when(personFactory.find(any(Long.class))).thenReturn(tested);
        UpdateEmployeeSalaryTypeCommand command = new UpdateEmployeeSalaryTypeCommand();
        command.personId = tested.getId();
        command.payrollType = PayrollTypeEnum.ACCORD;
        //Then
        handler.handle(command);
        //Then
        assertEquals(PayrollTypeEnum.ACCORD, tested.getPayrollType());
    }

    @Test
    public void shouldReturnValidCommandResult() {
        //Given
        Person person = createPerson(PayrollTypeEnum.HOURLY);
        when(personFactory.find(any(Long.class))).thenReturn(person);
        UpdateEmployeeSalaryTypeCommand command = new UpdateEmployeeSalaryTypeCommand();
        command.personId = person.getId();
        command.payrollType = PayrollTypeEnum.ACCORD;
        //Then
        CommandResult tested = handler.handle(command);
        //Then
        assertEquals(CommandResult.Status.OK, tested.status);
        assertEquals(person.getId(),tested.entityId);
        assertEquals("EmployeePayrollTypeUpdated",tested.body);
    }





    private Person createPerson(PayrollTypeEnum payrollType) {
        Person person = new Person.PersonBuilder(personRepository, beanValidator)
                .active(true)
                .id(1L)
                .nr(1L)
                .groupId(1L)
                .name("test_name")
                .surname("test_surname")
                .payrollType(payrollType)
                .build();
        person.create();
        return person;
    }
}