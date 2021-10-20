package com.fungisearch.fudriver.payroll.salary.command.model.bonus;

import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollBonusRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "payroll_percentage_bonus")
@DiscriminatorValue("payroll_percentage_bonus")
public class PayrollPercentageBonus extends PayrollBonus {

    @Override
    public long getValue(PersonMonthTotals totals) {
        long result = 0;
        switch (totals.payrollType) {
            case HOURLY:
                result = (totals.hoursSalary * param) / 100;
                break;
            case ACCORD:
                result = (totals.harvestSalary * param) / 100;
                break;
            default:
                throw new IllegalStateException("Not valid payroll type " + totals.payrollType);
        }
        return result;
    }

private PayrollPercentageBonus(){}


    public PayrollPercentageBonus(PayrollBonusRepository payrollBonusRepository, BeanValidator beanValidator) {
        super(payrollBonusRepository,beanValidator);
    }

    void create() {
        beanValidator.validate(this);
        payrollBonusRepository.save(this);
    }


    public static class PayrollPercentageBonusBuilder {
        private PayrollBonusRepository payrollBonusRepository;
        private BeanValidator beanValidator;
        private String name;
        private long param;

        public PayrollPercentageBonusBuilder(PayrollBonusRepository payrollBonusRepository, BeanValidator beanValidator) {
            this.payrollBonusRepository = payrollBonusRepository;
            this.beanValidator = beanValidator;
        }

        public PayrollPercentageBonusBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PayrollPercentageBonusBuilder param(long param) {
            this.param = param;
            return this;
        }

        public PayrollPercentageBonus build() {
            PayrollPercentageBonus payrollPercentageBonus = new PayrollPercentageBonus(payrollBonusRepository, beanValidator);
            payrollPercentageBonus.name = name;
            payrollPercentageBonus.param = param;
            payrollPercentageBonus.isActive = true;
            payrollPercentageBonus.create();
            return payrollPercentageBonus;
        }
    }
}