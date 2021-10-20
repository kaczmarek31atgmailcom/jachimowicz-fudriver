package com.fungisearch.fudriver.payroll.wage.query.dto;

import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;

/**
 * Do pokazania przypisań do stawek akordowych o godzinowych.
 */
public class PersonWageDto {

    public long id;
    public long nr;
    public String name;
    public String surname;
    public PayrollTypeEnum payrollType;
    public long wageHeaderId;
    public String wageHeaderName;
    public long hourlyRegularWage;
    public long hourlySundayWage;
    public long hourlyBonusWage;
    public long version;
}
