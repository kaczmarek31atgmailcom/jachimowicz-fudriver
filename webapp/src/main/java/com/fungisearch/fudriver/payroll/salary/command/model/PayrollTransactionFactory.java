package com.fungisearch.fudriver.payroll.salary.command.model;

import com.fungisearch.fudriver.payroll.salary.command.repository.PayrollTransactionRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayrollTransactionFactory {
    private transient PayrollTransactionRepository payrollTransactionRepository;
    private transient BeanValidator beanValidator;

    @Autowired
    public PayrollTransactionFactory(PayrollTransactionRepository payrollTransactionRepository, BeanValidator beanValidator) {
        this.payrollTransactionRepository = payrollTransactionRepository;
        this.beanValidator = beanValidator;
    }

    public PayrollIncreaseTransaction.PayrollIncreaseTransactionBuilder getIncreaseTransactionBuilder(){
        return new PayrollIncreaseTransaction.PayrollIncreaseTransactionBuilder(payrollTransactionRepository,beanValidator);
    }

    public PayrollDecreaseTransaction.PayrollDecreaseTransactionBuilder getDecreaseTransactionBuilder(){
        return new PayrollDecreaseTransaction.PayrollDecreaseTransactionBuilder(payrollTransactionRepository,beanValidator);
    }
}
