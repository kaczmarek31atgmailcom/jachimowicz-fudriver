package com.fungisearch.fudriver.payroll.salary.command.model.bonus;

import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollBonusRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayrollBonusFactory {

    private final PayrollBonusRepository payrollBonusRepository;
    private final BeanValidator beanValidator;

    @Autowired
    public PayrollBonusFactory(PayrollBonusRepository payrollBonusRepository, BeanValidator beanValidator) {
        this.payrollBonusRepository = payrollBonusRepository;
        this.beanValidator = beanValidator;
    }

    PayrollFixedBonus.PayrollFixedBonusBuilder getPayrollFixedBonusBuilder() {
        return new PayrollFixedBonus.PayrollFixedBonusBuilder(payrollBonusRepository, beanValidator);
    }

    PayrollPercentageBonus.PayrollPercentageBonusBuilder getPercentageBonusBuilder() {
        return new PayrollPercentageBonus.PayrollPercentageBonusBuilder(payrollBonusRepository, beanValidator);
    }

    BonusPersonMonthAssignment.BonusPersonMonthAssignmentBuilder getBonusPersonMonthAssignementBuilder() {
        return new BonusPersonMonthAssignment.BonusPersonMonthAssignmentBuilder(payrollBonusRepository, beanValidator);
    }

    List<BonusPersonMonthAssignment> findOpenMonthBonusAssignements(PayrollBonus bonus) {
        List<BonusPersonMonthAssignment> result = payrollBonusRepository.findAssignedBonusesInNotPayedMonths(bonus);
        if (!(result.isEmpty())) {
            for (BonusPersonMonthAssignment bonusPersonMonthAssignement : result) {
                bonusPersonMonthAssignement.payrollBonusRepository = payrollBonusRepository;
                bonusPersonMonthAssignement.beanValidator = beanValidator;
            }
        }
        return result;
    }

    PayrollBonus findBonus(long id) {
        PayrollBonus bonus = payrollBonusRepository.findBonus(id);
        if (bonus != null) {
            bonus.payrollBonusRepository = payrollBonusRepository;
            bonus.beanValidator = beanValidator;
        }
        return bonus;
    }

}
