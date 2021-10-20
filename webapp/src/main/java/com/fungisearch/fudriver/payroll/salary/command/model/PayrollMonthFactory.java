package com.fungisearch.fudriver.payroll.salary.command.model;

import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollMonthRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PayrollMonthFactory {

    private PayrollMonthRepository payrollMonthRepository;
    private BeanValidator beanValidator;

    @Autowired
    public PayrollMonthFactory(PayrollMonthRepository payrollMonthRepository, BeanValidator beanValidator) {
        this.payrollMonthRepository = payrollMonthRepository;
        this.beanValidator = beanValidator;
    }

    public PayrollMonth findByDay(Date day){
        PayrollMonth payrollMonth = payrollMonthRepository.findByDay(day);
        payrollMonth.payrollMonthRepository = payrollMonthRepository;
        payrollMonth.beanValidator = beanValidator;
        return payrollMonth;
    }

    public PayrollMonth find(long bonusId) {
        PayrollMonth payrollMonth = payrollMonthRepository.find(bonusId);
        if(payrollMonth != null){
            payrollMonth.payrollMonthRepository = payrollMonthRepository;
            payrollMonth.beanValidator = beanValidator;
        }
        return payrollMonth;
    }
}
