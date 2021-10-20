package com.fungisearch.fudriver.payroll.salary.query.dto.payed;

import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;

public class PayedPersonSalaryHarvestDetailDto {
    public long typeId;
    public String typeName;
    public long typeWeight;
    public SalaryDayTypeEnum dayType;
    public long wageId;
    public long wageValue;
    public long kgAmount;
    public long moneyAmount;
}
