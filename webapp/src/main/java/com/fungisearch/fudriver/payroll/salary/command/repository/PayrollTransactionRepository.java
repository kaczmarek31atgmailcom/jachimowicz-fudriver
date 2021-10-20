package com.fungisearch.fudriver.payroll.salary.command.repository;


import com.fungisearch.fudriver.payroll.salary.command.model.PayrollTransaction;

public interface PayrollTransactionRepository {
    void save(PayrollTransaction transaction);
}
