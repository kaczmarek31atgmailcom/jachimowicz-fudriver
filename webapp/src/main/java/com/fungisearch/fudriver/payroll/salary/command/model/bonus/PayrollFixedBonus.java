package com.fungisearch.fudriver.payroll.salary.command.model.bonus;

import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollBonusRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "payroll_fixed_bonus")
@DiscriminatorValue("payroll_fixed_bonus")
public class PayrollFixedBonus extends PayrollBonus {

    @Override
    public long getValue(PersonMonthTotals totals) {
        return param;
    }

    void create(){
        beanValidator.validate(this);
        payrollBonusRepository.save(this);
    }

    private PayrollFixedBonus(){}

    public PayrollFixedBonus(PayrollBonusRepository payrollBonusRepository, BeanValidator beanValidator){
        super(payrollBonusRepository,beanValidator);
    }

    public static class PayrollFixedBonusBuilder{
        private PayrollBonusRepository payrollBonusRepository;
        private BeanValidator beanValidator;
        private String name;
        private long param;

        public PayrollFixedBonusBuilder(PayrollBonusRepository payrollBonusRepository,BeanValidator beanValidator){
            this.payrollBonusRepository = payrollBonusRepository;
            this.beanValidator = beanValidator;
        }

        public PayrollFixedBonusBuilder name(String name){
            this.name = name;
            return this;
        }

        public PayrollFixedBonusBuilder param(long param){
            this.param = param;
            return this;
        }

        public PayrollFixedBonus build(){
            PayrollFixedBonus payrollFixedBonus = new PayrollFixedBonus(payrollBonusRepository,beanValidator);
            payrollFixedBonus.name = name;
            payrollFixedBonus.param = param;
            payrollFixedBonus.isActive = true;
            payrollFixedBonus.create();
            return payrollFixedBonus;
        }
    }
}
