package com.fungisearch.fudriver.payroll.salary.command.model;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollPayoffRepository;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "work_time_salary")
public class WorkTimeSalary extends BaseEntity {
    public transient PayrollPayoffRepository payrollPayoffRepository;
    public transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "payoff_detail_id")
    private MonthlyPayoffDetail monthlyPayoffDetail;

    @Column(name = "regular_rate")
    @NotNull
    private long regularRate;

    @Column(name = "regular_minutes")
    @NotNull
    private long regularMinutes;

    @Column(name = "sunday_rate")
    @NotNull
    private long sundayRate;

    @Column(name = "sunday_minutes")
    @NotNull
    private long sundayMinutes;

    @Column(name = "bonus_rate")
    @NotNull
    private long bonusRate;

    @Column(name = "bonus_minutes")
    @NotNull
    private long bonusMinutes;

    @ManyToOne
    @JoinColumn(name = "month_id")
    private PayrollMonth month;


    private WorkTimeSalary() {
    }

    public WorkTimeSalary(PayrollPayoffRepository payrollPayoffRepository, BeanValidator beanValidator) {
        this.payrollPayoffRepository = payrollPayoffRepository;
        this.beanValidator = beanValidator;
    }

    public Long getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public long getRegularRate() {
        return regularRate;
    }

    public long getRegularMinutes() {
        return regularMinutes;
    }

    public long getSundayRate() {
        return sundayRate;
    }

    public long getSundayMinutes() {
        return sundayMinutes;
    }

    public long getBonusRate() {
        return bonusRate;
    }

    public long getBonusMinutes() {
        return bonusMinutes;
    }

    public PayrollMonth getMonth() {
        return month;
    }

    private void create() {
        beanValidator.validate(this);
        payrollPayoffRepository.save(this);
    }

    public static class WorkTimeSalaryBuilder {
        private final PayrollPayoffRepository payrollPayoffRepository;
        private final BeanValidator beanValidator;
        private PayrollMonth payrollMonth;
        private MonthlyPayoffDetail monthlyPayoffDetail;
        private Person person;
        private long minutes;
        private SalaryDayTypeEnum salaryDayType;

        public WorkTimeSalaryBuilder(PayrollPayoffRepository payrollPayoffRepository, BeanValidator beanValidator) {
            this.payrollPayoffRepository = payrollPayoffRepository;
            this.beanValidator = beanValidator;
        }

        public WorkTimeSalaryBuilder payrollMonth(PayrollMonth payrollMonth) {
            this.payrollMonth = payrollMonth;
            return this;
        }

        public WorkTimeSalaryBuilder salaryDayType(SalaryDayTypeEnum salaryDayType) {
            this.salaryDayType = salaryDayType;
            return this;
        }

        public WorkTimeSalaryBuilder person(Person person) {
            this.person = person;
            return this;
        }

        public WorkTimeSalaryBuilder minutes(long minutes) {
            this.minutes = minutes;
            return this;
        }


        public WorkTimeSalaryBuilder monthlyPayoffDetail(MonthlyPayoffDetail monthlyPayoffDetail) {
            this.monthlyPayoffDetail = monthlyPayoffDetail;
            return this;
        }

        public WorkTimeSalary build() {
            WorkTimeSalary workTimeSalary = new WorkTimeSalary(payrollPayoffRepository, beanValidator);
            workTimeSalary.person = person;
            workTimeSalary.month = payrollMonth;
            workTimeSalary.monthlyPayoffDetail = monthlyPayoffDetail;
            workTimeSalary.regularRate = person.getRegularWage();
            workTimeSalary.sundayRate = person.getSundayWage();
            workTimeSalary.bonusRate = person.getBonusWage();
            switch (salaryDayType) {
                case REGULAR_DAY:
                    workTimeSalary.regularMinutes = this.minutes;
                    workTimeSalary.sundayMinutes = 0L;
                    workTimeSalary.bonusMinutes = 0L;
                    break;
                case SUNDAY:
                    workTimeSalary.sundayMinutes = this.minutes;
                    workTimeSalary.regularMinutes = 0L;
                    workTimeSalary.bonusMinutes = 0L;
                    break;
                case BONUS_DAY:
                    workTimeSalary.bonusMinutes = this.minutes;
                    workTimeSalary.regularMinutes = 0L;
                    workTimeSalary.sundayMinutes = 0L;
                    break;
                default:
                    throw new IllegalStateException("Not valid day type");
            }
            workTimeSalary.create();
            return workTimeSalary;
        }
    }
}
