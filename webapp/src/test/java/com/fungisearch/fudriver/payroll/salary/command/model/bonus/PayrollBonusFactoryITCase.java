package com.fungisearch.fudriver.payroll.salary.command.model.bonus;

import com.fungisearch.fudriver.payroll.salary.command.model.PayrollMonth;
import com.fungisearch.fudriver.payroll.salary.command.model.PayrollMonthFactory;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.testTools.CreatePerson;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = {"/test-spring.xml"})
public class PayrollBonusFactoryITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CreatePerson createPerson;
    @Autowired
    private PayrollMonthFactory payrollMonthFactory;
    @Autowired
    private PayrollBonusFactory payrollBonusFactory;

    @Test
    public void shouldFindBonusPersonMonthAssignement(){
        //Given
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        Person person = createPerson.create();
        PayrollMonth payrollMonth = payrollMonthFactory.findByDay(formatter.parseDateTime("2017-03-01").toDate());
        assertFalse(payrollMonth.isClosed());
        PayrollFixedBonus bonus = payrollBonusFactory.getPayrollFixedBonusBuilder().name("test_fixed_bonus").param(10L).build();
        payrollBonusFactory.getBonusPersonMonthAssignementBuilder().payrollBonus(bonus).person(person).payrollMonth(payrollMonth).build();
        //When
        List<BonusPersonMonthAssignment> tested = payrollBonusFactory.findOpenMonthBonusAssignements(bonus);
        //Then
        assertEquals(1,tested.size());
        assertEquals(bonus,tested.get(0).getPayrollBonus());
        assertEquals(person,tested.get(0).getPerson());
        assertEquals(payrollMonth,tested.get(0).getPayrollMonth());
    }

    @Test
    public void shouldFindBonusPersonMonthAssignementWhenOneMonthIsClosed(){
        //Given
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        Person person = createPerson.create();
        PayrollMonth closedMonth = payrollMonthFactory.findByDay(formatter.parseDateTime("2017-03-01").toDate());
        PayrollMonth payrollMonth = payrollMonthFactory.findByDay(formatter.parseDateTime("2017-04-01").toDate());
        assertFalse(payrollMonth.isClosed());
        closedMonth.close();
        assertTrue(closedMonth.isClosed());
        PayrollFixedBonus bonus = payrollBonusFactory.getPayrollFixedBonusBuilder().name("test_fixed_bonus").param(10L).build();
        payrollBonusFactory.getBonusPersonMonthAssignementBuilder().payrollBonus(bonus).person(person).payrollMonth(payrollMonth).build();
        payrollBonusFactory.getBonusPersonMonthAssignementBuilder().payrollBonus(bonus).person(person).payrollMonth(closedMonth).build();
        //When
        List<BonusPersonMonthAssignment> tested = payrollBonusFactory.findOpenMonthBonusAssignements(bonus);
        //Then
        assertEquals(1,tested.size());
        assertEquals(bonus,tested.get(0).getPayrollBonus());
        assertEquals(person,tested.get(0).getPerson());
        assertEquals(payrollMonth,tested.get(0).getPayrollMonth());
    }
}