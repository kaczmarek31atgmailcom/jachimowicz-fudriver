package com.fungisearch.fudriver.payroll.salary.command.model.bonus;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = {"/test-spring.xml"})
public class DeleteBonusCommandHandlerITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private PayrollBonusFactory payrollBonusFactory;

    @Test
    public void shouldIncactivateBonus(){
        //Given
        PayrollBonus tested = payrollBonusFactory.getPayrollFixedBonusBuilder().name("test").param(1L).build();
        assertTrue(tested.isActive);
        //When
        tested.inactivate();
        //Then
        assertFalse(tested.isActive);
    }
}