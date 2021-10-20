package com.fungisearch.fudriver.payroll.salary.command.model.bonus;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.exception.AssignedOpenMonthsException;
import com.fungisearch.fudriver.payroll.salary.command.model.PayrollMonth;
import com.fungisearch.fudriver.payroll.salary.command.model.PayrollMonthFactory;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.testTools.CreatePerson;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;


import java.util.Date;

import static org.junit.Assert.*;

//@ContextConfiguration(locations = {"/test-spring.xml"})
//public class UpdatePersonBonusMonthlyAssignmentCommandHandlerTest extends AbstractTransactionalJUnit4SpringContextTests{
public class UpdatePersonBonusMonthlyAssignmentCommandHandlerTest {
    @Autowired
    private CreatePerson createPerson;
    @Autowired
    private PayrollBonusFactory payrollBonusFactory;
    @Autowired
    private PayrollMonthFactory payrollMonthFactory;
    @Autowired
    private BonusPersonMonthAssignmentFactory bonusPersonMonthAssignmentFactory;
    @Autowired
    private UpdatePersonBonusMonthlyAssignmentCommandHandler handler;


    public void shouldCreateProperCommandResult(){
        //Given
        DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd");
        Date day = df.parseDateTime("2017-03-01").toDate();
        Person person = createPerson.create();
        PayrollBonus bonus = payrollBonusFactory.getPayrollFixedBonusBuilder().param(1).name("jeden").build();
        PayrollMonth month = payrollMonthFactory.findByDay(day);
        UpdatePersonBonusMonthlyAssignmentCommand command = new UpdatePersonBonusMonthlyAssignmentCommand();
        command.personId = person.getId();
        command.bonusId = bonus.id;
        command.monthId = month.getId();
        //When
        CommandResult commandResult = handler.handle(command);
        //Then
        Assert.assertEquals(CommandResult.Status.OK,commandResult.status);
        Assert.assertEquals("BonusAssigned",commandResult.body);
    }


    public void shouldCreateAssignment(){
        //Given
        DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd");
        Date day = df.parseDateTime("2017-03-01").toDate();
        Person person = createPerson.create();
        PayrollBonus bonus = payrollBonusFactory.getPayrollFixedBonusBuilder().param(1).name("jeden").build();
        PayrollMonth month = payrollMonthFactory.findByDay(day);
        UpdatePersonBonusMonthlyAssignmentCommand command = new UpdatePersonBonusMonthlyAssignmentCommand();
        command.personId = person.getId();
        command.bonusId = bonus.id;
        command.monthId = month.getId();
        //When
        CommandResult commandResult = handler.handle(command);
        BonusPersonMonthAssignment tested = bonusPersonMonthAssignmentFactory.find(person,bonus,month);
        //Then
        Assert.assertNotNull(tested);
        Assert.assertEquals(person,tested.getPerson());
        Assert.assertEquals(bonus,tested.getPayrollBonus());
        Assert.assertEquals(month,tested.getPayrollMonth());
    }


    public void shouldDeleteAssignment(){
        //Given
        DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd");
        Date day = df.parseDateTime("2017-03-01").toDate();
        Person person = createPerson.create();
        PayrollBonus bonus = payrollBonusFactory.getPayrollFixedBonusBuilder().param(1).name("jeden").build();
        PayrollMonth month = payrollMonthFactory.findByDay(day);
        UpdatePersonBonusMonthlyAssignmentCommand command = new UpdatePersonBonusMonthlyAssignmentCommand();
        command.personId = person.getId();
        command.bonusId = bonus.id;
        command.monthId = month.getId();
        handler.handle(command);
        BonusPersonMonthAssignment assignment = bonusPersonMonthAssignmentFactory.find(person,bonus,month);
        Assert.assertNotNull(assignment);
        Assert.assertEquals(person,assignment.getPerson());
        Assert.assertEquals(bonus,assignment.getPayrollBonus());
        Assert.assertEquals(month,assignment.getPayrollMonth());
        //When
        handler.handle(command);
        BonusPersonMonthAssignment tested = bonusPersonMonthAssignmentFactory.find(person,bonus,month);
        //Then
        Assert.assertNull(tested);
    }
}
