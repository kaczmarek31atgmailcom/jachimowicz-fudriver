package com.fungisearch.fudriver.payroll.salary.query.dto.notPayed;


import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;

public class HarvestByPersonAndWageDto {
    public long personId;
    public long wage;
    public long totalKg;
    public SalaryDayTypeEnum dayType;
    public long salary;
}
