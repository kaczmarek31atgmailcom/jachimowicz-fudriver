package com.fungisearch.fudriver.payroll.salary.command.repository;


import com.fungisearch.fudriver.payroll.salary.command.model.PayrollMonth;

import java.util.Date;

public interface PayrollMonthRepository {
    void save(PayrollMonth payrollMonth);

    PayrollMonth findByDay(Date day);

    PayrollMonth find(long bonusId);
}
