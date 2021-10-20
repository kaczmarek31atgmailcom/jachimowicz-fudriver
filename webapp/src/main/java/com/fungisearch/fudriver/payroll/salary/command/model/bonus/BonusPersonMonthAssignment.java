package com.fungisearch.fudriver.payroll.salary.command.model.bonus;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.payroll.salary.command.model.PayrollMonth;
import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollBonusRepository;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;

@Entity
@Table(name = "bonus_person_month", uniqueConstraints={@UniqueConstraint(columnNames = {"person_id","payroll_month_id","bonus_id"})})
public class BonusPersonMonthAssignment extends BaseEntity {

    public transient PayrollBonusRepository payrollBonusRepository;
    public transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "payroll_month_id", nullable = false)
    private PayrollMonth payrollMonth;

    @ManyToOne
    @JoinColumn(name = "bonus_id", nullable = false)
    private PayrollBonus payrollBonus;

    private BonusPersonMonthAssignment() {
    }

    public BonusPersonMonthAssignment(PayrollBonusRepository payrollBonusRepository, BeanValidator beanValidator) {
        this.payrollBonusRepository = payrollBonusRepository;
        this.beanValidator = beanValidator;
    }

    public Long getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public PayrollMonth getPayrollMonth() {
        return payrollMonth;
    }

    public PayrollBonus getPayrollBonus() {
        return payrollBonus;
    }

    private void create() {
        beanValidator.validate(this);
        payrollBonusRepository.save(this);
    }

    public void delete(){
        if(this.getPayrollMonth().isClosed()){
            throw new IllegalStateException("Deleting assigned bonus on closed payroll month");
        }
        payrollBonusRepository.delete(this);
    }

    public static class BonusPersonMonthAssignmentBuilder {
        private PayrollBonusRepository payrollBonusRepository;
        private BeanValidator beanValidator;
        private Person person;
        private PayrollMonth payrollMonth;
        private PayrollBonus payrollBonus;

        public BonusPersonMonthAssignmentBuilder(PayrollBonusRepository payrollBonusRepository, BeanValidator beanValidator) {
            this.payrollBonusRepository = payrollBonusRepository;
            this.beanValidator = beanValidator;
        }

        public BonusPersonMonthAssignmentBuilder person(Person person) {
            this.person = person;
            return this;
        }

        public BonusPersonMonthAssignmentBuilder payrollMonth(PayrollMonth payrollMonth) {
            this.payrollMonth = payrollMonth;
            return this;
        }

        public BonusPersonMonthAssignmentBuilder payrollBonus(PayrollBonus payrollBonus) {
            this.payrollBonus = payrollBonus;
            return this;
        }

        public BonusPersonMonthAssignment build() {
            BonusPersonMonthAssignment bonusPersonMonthAssignment = new BonusPersonMonthAssignment(payrollBonusRepository, beanValidator);
            bonusPersonMonthAssignment.person = person;
            bonusPersonMonthAssignment.payrollMonth = payrollMonth;
            bonusPersonMonthAssignment.payrollBonus = payrollBonus;
            bonusPersonMonthAssignment.create();
            return bonusPersonMonthAssignment;
        }
    }
}
