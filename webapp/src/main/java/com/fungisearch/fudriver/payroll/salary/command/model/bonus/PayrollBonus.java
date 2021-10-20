package com.fungisearch.fudriver.payroll.salary.command.model.bonus;


import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.exception.AssignedOpenMonthsException;
import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollBonusRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "payroll_bonus")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class PayrollBonus extends BaseEntity{

    public transient PayrollBonusRepository payrollBonusRepository;
    public transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "param")
    Long param;

    @Column(name = "active")
    boolean isActive = true;

    public PayrollBonus(){};

    public PayrollBonus(PayrollBonusRepository payrollBonusRepository, BeanValidator beanValidator){
        this.payrollBonusRepository = payrollBonusRepository;
        this.beanValidator = beanValidator;
    }

    public Long getId() {
        return id;
    }

    public abstract long getValue(PersonMonthTotals totals);

    public void inactivate(){
        payrollBonusRepository.findAssignedBonusesInNotPayedMonths(this).forEach(this::removeBonusPersonMonthAssignement);

        this.isActive = false;
        beanValidator.validate(this);
    }

    private void removeBonusPersonMonthAssignement(BonusPersonMonthAssignment assignment){
        assignment.payrollBonusRepository = payrollBonusRepository;
        assignment.beanValidator = beanValidator;
        assignment.delete();
    }

    abstract void create();
}
