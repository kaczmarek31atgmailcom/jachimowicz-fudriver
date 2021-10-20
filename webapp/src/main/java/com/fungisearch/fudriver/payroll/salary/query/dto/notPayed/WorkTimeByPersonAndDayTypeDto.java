package com.fungisearch.fudriver.payroll.salary.query.dto.notPayed;


import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;

import java.util.Date;

public class WorkTimeByPersonAndDayTypeDto {
    public long personId;
    public SalaryDayTypeEnum dayType;
    public Date startTime;
    public Date endTime;

    @Override
    public String toString() {
        return "WorkTimeByPersonAndDayTypeDto{" +
                "personId=" + personId +
                ", dayType=" + dayType +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
