package com.fungisearch.fudriver.payroll.salary.command.model.bonus;

import com.fungisearch.fudriver.payroll.salary.command.model.PayrollMonth;
import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollBonusRepository;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BonusPersonMonthAssignmentFactory {
    private final PayrollBonusRepository payrollBonusRepository;
    private final BeanValidator beanValidator;

    @Autowired
    public BonusPersonMonthAssignmentFactory(PayrollBonusRepository payrollBonusRepository, BeanValidator beanValidator) {
        this.payrollBonusRepository = payrollBonusRepository;
        this.beanValidator = beanValidator;
    }

    public boolean changeBonusAssignment(Person person, PayrollBonus bonus, PayrollMonth month) {
        boolean result;
        BonusPersonMonthAssignment bonusPersonMonthAssignment = payrollBonusRepository.findBonusPersonMonthAssignment(person, bonus, month);
        if (bonusPersonMonthAssignment == null) {
            BonusPersonMonthAssignment assignment = new BonusPersonMonthAssignment.BonusPersonMonthAssignmentBuilder(payrollBonusRepository, beanValidator)
                    .payrollBonus(bonus)
                    .payrollMonth(month)
                    .person(person)
                    .build();
            result = true;
        } else {
            bonusPersonMonthAssignment.payrollBonusRepository = payrollBonusRepository;
            bonusPersonMonthAssignment.beanValidator = beanValidator;
            bonusPersonMonthAssignment.delete();
            result = false;
        }
        return result;
    }

    public BonusPersonMonthAssignment find(Person person, PayrollBonus bonus, PayrollMonth month) {
        BonusPersonMonthAssignment assignment = payrollBonusRepository.findBonusPersonMonthAssignment(person, bonus, month);
        if (assignment != null) {
            assignment.payrollBonusRepository = payrollBonusRepository;
            assignment.beanValidator = beanValidator;
        }
        return assignment;
    }

    public List<PayrollBonus> findAssignedBonusesForPersonInMonth(Person person, PayrollMonth month){
        List<PayrollBonus> result = payrollBonusRepository.findAssignedBonusesForPersonInMonth(person,month);
        if(!(result.isEmpty())){
            for(PayrollBonus bonus: result){
                bonus.payrollBonusRepository = payrollBonusRepository;
                bonus.beanValidator = beanValidator;
            }
        }
        return result;
    }
}
