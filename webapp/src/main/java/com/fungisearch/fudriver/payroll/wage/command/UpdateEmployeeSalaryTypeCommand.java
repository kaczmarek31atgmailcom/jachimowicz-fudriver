package com.fungisearch.fudriver.payroll.wage.command;


import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;

public class UpdateEmployeeSalaryTypeCommand {
    public long personId;
    public PayrollTypeEnum payrollType;
}
