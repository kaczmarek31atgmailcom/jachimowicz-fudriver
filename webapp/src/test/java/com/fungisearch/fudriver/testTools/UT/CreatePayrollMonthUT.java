package com.fungisearch.fudriver.testTools.UT;


import com.fungisearch.fudriver.payroll.salary.command.model.PayrollMonth;
import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollMonthRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import java.util.Date;

public class CreatePayrollMonthUT {

    private final PayrollMonthRepository payrollMonthRepository;
    private final BeanValidator beanValidator;

    public CreatePayrollMonthUT(PayrollMonthRepository payrollMonthRepository, BeanValidator beanValidator) {
        this.payrollMonthRepository = payrollMonthRepository;
        this.beanValidator = beanValidator;
    }

    public PayrollMonth create(Date date) {
        PayrollMonth month = new PayrollMonth.PayrollMonthBuilder(payrollMonthRepository, beanValidator)
                .firstDay(date)
                .build();
        return month;
    }

}
