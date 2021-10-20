package com.fungisearch.fudriver.payroll.wage.query.dto;

import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;

public class WageDto {
    public long id;
    public long typeId;
    public String typeName;
    public double typeWeight;
    public SalaryDayTypeEnum dayType;
    public long value;

}
