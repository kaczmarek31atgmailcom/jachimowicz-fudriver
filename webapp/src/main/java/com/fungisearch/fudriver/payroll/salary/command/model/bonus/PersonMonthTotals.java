package com.fungisearch.fudriver.payroll.salary.command.model.bonus;

import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;

/**
 * Created by marcin on 18.07.17.
 */
public class PersonMonthTotals {
    public PayrollTypeEnum payrollType;
    public long harvestSalary = 0;
    public long hoursSalary = 0;
}
