package com.fungisearch.fudriver.payroll.salary.command.repository;


import com.fungisearch.fudriver.payroll.salary.command.model.PayrollMonth;
import com.fungisearch.fudriver.payroll.salary.command.model.bonus.BonusPersonMonthAssignment;
import com.fungisearch.fudriver.payroll.salary.command.model.bonus.PayrollBonus;
import com.fungisearch.fudriver.payroll.salary.command.model.bonus.PayrollFixedBonus;
import com.fungisearch.fudriver.payroll.salary.command.model.bonus.PayrollPercentageBonus;
import com.fungisearch.fudriver.person.person.command.model.Person;

import java.util.List;

public interface PayrollBonusRepository {
    void save(PayrollFixedBonus payrollFixedBonus);
    void save(PayrollPercentageBonus payrollPercentageBonus);
    void save(BonusPersonMonthAssignment bonusPersonMonthAssignement);

    List<BonusPersonMonthAssignment> findAssignedBonusesInNotPayedMonths(PayrollBonus bonus);

    PayrollBonus findBonus(long id);
    BonusPersonMonthAssignment findBonusPersonMonthAssignment(Person person, PayrollBonus bonus, PayrollMonth month);

    void delete(BonusPersonMonthAssignment bonusPersonMonthAssignment);

    List<PayrollBonus> findAssignedBonusesForPersonInMonth(Person person, PayrollMonth month);
}
