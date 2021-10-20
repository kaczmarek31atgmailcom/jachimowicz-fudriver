package com.fungisearch.fudriver.payroll.salary.command.model;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollPayoffRepository;
import com.fungisearch.fudriver.payroll.wage.command.model.Wage;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "harvest_salary")
public class HarvestSalary extends BaseEntity {

    public transient PayrollPayoffRepository payrollPayoffRepository;
    public transient BeanValidator beanValidator;


    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "salary_day_type")
    @NotNull
    private SalaryDayTypeEnum dayType;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "wage_id", nullable = false)
    private Wage wage;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "month_id")
    private PayrollMonth month;

    @ManyToOne
    @JoinColumn(name = "payoff_detail_id")
    @NotNull
    private MonthlyPayoffDetail monthlyPayoffDetail;

    @Column(name = "wage_value")
    private long wageValue;

    @Column(name = "amount_kg")
    private long amountG;

    @Column(name = "amount_money")
    private long amountMoney;

    private HarvestSalary() {
    }


    public HarvestSalary(PayrollPayoffRepository payrollPayoffRepository, BeanValidator beanValidator) {
        this.payrollPayoffRepository = payrollPayoffRepository;
        this.beanValidator = beanValidator;

    }

    public Long getId() {
        return id;
    }

    public SalaryDayTypeEnum getDayType() {
        return dayType;
    }

    public Type getType() {
        return type;
    }

    public Wage getWage() {
        return wage;
    }

    public Person getPerson() {
        return person;
    }

    public PayrollMonth getMonth() {
        return month;
    }

    public long getWageValue() {
        return wageValue;
    }

    public long getAmountG() {
        return amountG;
    }

    public long getAmountMoney() {
        return amountMoney;
    }

    public MonthlyPayoffDetail getMonthlyPayoffDetail() {
        return monthlyPayoffDetail;
    }

    private void create() {
        beanValidator.validate(this);
        payrollPayoffRepository.save(this);
    }

    public void delete() {
        beanValidator.validate(this);
        payrollPayoffRepository.delete(this);
    }

    public static class HarvestSalaryBuilder {
        private PayrollPayoffRepository payrollPayoffRepository;
        private BeanValidator beanValidator;
        private final transient Logger logger = LoggerFactory.getLogger(this.getClass().getName());
        private Person person;
        private Type type;
        private Wage wage;
        private long amountG;
        private SalaryDayTypeEnum dayType;
        private PayrollMonth month;
        private MonthlyPayoffDetail monthlyPayoffDetail;


        public HarvestSalaryBuilder(PayrollPayoffRepository payrollPayoffRepository, BeanValidator beanValidator) {
            this.payrollPayoffRepository = payrollPayoffRepository;
            this.beanValidator = beanValidator;
        }

        public HarvestSalaryBuilder person(Person person) {
            this.person = person;
            return this;
        }

        public HarvestSalaryBuilder type(Type type) {
            this.type = type;
            return this;
        }

        public HarvestSalaryBuilder amountG(long amountG) {
            this.amountG = amountG;
            return this;
        }

        public HarvestSalaryBuilder wage(Wage wage) {
            this.wage = wage;
            return this;
        }

        public HarvestSalaryBuilder salaryDayType(SalaryDayTypeEnum dayType) {
            this.dayType = dayType;
            return this;
        }

        public HarvestSalaryBuilder month(PayrollMonth payrollMonth) {
            this.month = payrollMonth;
            return this;
        }

        public HarvestSalaryBuilder monthlyPayoffDetail(MonthlyPayoffDetail monthlyPayoffDetail) {
            this.monthlyPayoffDetail = monthlyPayoffDetail;
            return this;
        }

        public HarvestSalary build() {
            HarvestSalary harvestSalary = new HarvestSalary(payrollPayoffRepository, beanValidator);
            harvestSalary.amountG = amountG;
            harvestSalary.wage = wage;
            harvestSalary.wageValue = wage.getValue();
            harvestSalary.dayType = dayType;
            harvestSalary.month = month;
            harvestSalary.person = person;
            harvestSalary.type = type;
            harvestSalary.amountMoney = (harvestSalary.amountG * harvestSalary.wageValue) / 1000;
            harvestSalary.monthlyPayoffDetail = monthlyPayoffDetail;
            harvestSalary.create();
            return harvestSalary;
        }
    }
}
