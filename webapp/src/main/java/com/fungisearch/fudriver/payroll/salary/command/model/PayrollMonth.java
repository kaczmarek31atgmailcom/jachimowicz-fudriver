package com.fungisearch.fudriver.payroll.salary.command.model;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollMonthRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payroll_month")
public class PayrollMonth extends BaseEntity {

    public transient PayrollMonthRepository payrollMonthRepository;
    public transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    Long id;

    @Column(name = "first_day", unique = true)
    Date firstDay;

    @Column(name = "is_closed")
    boolean isClosed;

    public boolean isClosed() {
        return isClosed;
    }

    private PayrollMonth() {
    }

    public PayrollMonth(PayrollMonthRepository payrollMonthRepository, BeanValidator beanValidator) {
        this.payrollMonthRepository = payrollMonthRepository;
        this.beanValidator = beanValidator;
    }

    public Long getId() {
        return id;
    }

    public Date getFirstDay() {
        return firstDay;
    }

    public boolean isDayInMonth(Date day) {
        DateTime calendarDay = new DateTime(firstDay);
        DateTime testedDay = new DateTime(day).withDayOfMonth(1);
        return calendarDay.equals(testedDay);
    }

    public void close() {
        if (this.isClosed) {
            throw new IllegalStateException("Payroll month already closed");
        }
        this.isClosed = true;
        beanValidator.validate(this);
        payrollMonthRepository.save(this);
    }

    private void create() {

    }

    public static class PayrollMonthBuilder {
        private PayrollMonthRepository payrollMonthRepository;
        private BeanValidator beanValidator;
        private Date day;

        public PayrollMonthBuilder(PayrollMonthRepository payrollMonthRepository, BeanValidator beanValidator) {
            this.payrollMonthRepository = payrollMonthRepository;
            this.beanValidator = beanValidator;
        }

        public PayrollMonthBuilder firstDay(Date day) {
            this.day = new DateTime(day).withDayOfMonth(1).toDate();
            return this;
        }

        public PayrollMonth build() {
            PayrollMonth payrollMonth = new PayrollMonth(payrollMonthRepository, beanValidator);
            payrollMonth.isClosed = false;
            payrollMonth.firstDay = day;
            payrollMonth.create();
            return payrollMonth;
        }
    }
}
