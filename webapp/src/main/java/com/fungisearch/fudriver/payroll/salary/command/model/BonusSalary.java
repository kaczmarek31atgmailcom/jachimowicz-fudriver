package com.fungisearch.fudriver.payroll.salary.command.model;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.payroll.salary.command.model.bonus.PayrollBonus;
import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollPayoffRepository;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;

@Entity
@Table(name = "bonus_salary")
public class BonusSalary extends BaseEntity {

    public transient PayrollPayoffRepository payrollPayoffRepository;
    public transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "payoff_detail_id")
    private MonthlyPayoffDetail monthlyPayoffDetail;

    @ManyToOne
    @JoinColumn(name = "month_id")
    private PayrollMonth month;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "bonus_id", nullable = false)
    private PayrollBonus bonus;

    @Column(name = "amount_money")
    private long amountMoney;

    private BonusSalary(){}

    public BonusSalary(PayrollPayoffRepository payrollPayoffRepository, BeanValidator beanValidator) {
        this.payrollPayoffRepository = payrollPayoffRepository;
        this.beanValidator = beanValidator;
    }

    public Long getId() {
        return id;
    }

    public MonthlyPayoffDetail getMonthlyPayoffDetail() {
        return monthlyPayoffDetail;
    }

    public PayrollMonth getMonth() {
        return month;
    }

    public Person getPerson() {
        return person;
    }

    public PayrollBonus getBonus() {
        return bonus;
    }

    public long getAmountMoney() {
        return amountMoney;
    }

    private void create(){
        beanValidator.validate(this);
        payrollPayoffRepository.save(this);
    }

    public static class BonusSalaryBuilder {
        private PayrollPayoffRepository payrollPayoffRepository;
        private BeanValidator beanValidator;
        private MonthlyPayoffDetail monthlyPayoffDetail;
        private PayrollMonth month;
        private Person person;
        private PayrollBonus bonus;
        private long amountMoney;

        public BonusSalaryBuilder(PayrollPayoffRepository payrollPayoffRepository, BeanValidator beanValidator) {
            this.payrollPayoffRepository = payrollPayoffRepository;
            this.beanValidator = beanValidator;
        }

        public BonusSalaryBuilder payoffDetail(MonthlyPayoffDetail detail){
            this.monthlyPayoffDetail = detail;
            return this;
        }

        public BonusSalaryBuilder month(PayrollMonth month){
            this.month = month;
            return this;
        }

        public BonusSalaryBuilder person(Person person){
            this.person = person;
            return this;
        }

        public BonusSalaryBuilder bonus(PayrollBonus bonus){
            this.bonus = bonus;
            return this;
        }

        public BonusSalaryBuilder amountMoney(long amountMoney){
            this.amountMoney = amountMoney;
            return this;
        }

        public BonusSalary build(){
            BonusSalary bonusSalary = new BonusSalary(payrollPayoffRepository,beanValidator);
            bonusSalary.monthlyPayoffDetail = this.monthlyPayoffDetail;
            bonusSalary.month = this.month;
            bonusSalary.person = this.person;
            bonusSalary.bonus = this.bonus;
            bonusSalary.amountMoney = this.amountMoney;
            bonusSalary.create();
            return bonusSalary;
        }
    }

}
