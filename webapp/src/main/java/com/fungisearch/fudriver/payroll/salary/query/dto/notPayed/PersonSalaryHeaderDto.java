package com.fungisearch.fudriver.payroll.salary.query.dto.notPayed;

import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;

public class PersonSalaryHeaderDto {
    public long id;
    public long nr;
    public String name;
    public String surname;
    public long groupId;
    public String groupName;
    public long totalHarvest;
    public long quality;
    public Long wageHeaderId;
    public PayrollTypeEnum payrollType;
    public long regularHarvestSalary;
    public long sundayHarvestSalary;
    public long bonusHarvestSalary;
    public long regularMinutes;
    public long sundayMinutes;
    public long bonusMinutes;
    public long regularHoursWage;
    public long sundayHoursWage;
    public long bonusHoursWage;
    public long regularHoursSalary;
    public long sundayHoursSalary;
    public long bonusHoursSalary;
    public int workDaysAmount;
    public boolean isValid = true;

    @Override
    public String toString() {
        return "PersonSalaryHeaderDto{" +
                "id=" + id +
                ", nr=" + nr +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
