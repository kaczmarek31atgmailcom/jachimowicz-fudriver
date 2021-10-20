package com.fungisearch.fudriver.payroll.salary.query.dto.payed;

import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;

import java.util.ArrayList;
import java.util.List;

public class PayedPersonSalaryHeaderDto {
    public long payoffDetailId;
    public long personId;
    public long personNr;
    public String personName;
    public String personSurname;
    public PayrollTypeEnum payrollType;
    public long regularHarvestSalary;
    public long sundayHarvestSalary;
    public long bonusHarvestSalary;
    public long regularWorkTimeSalary;
    public long sundayWorkTimeSalary;
    public long bonusWorkTimeSalary;
    public List<PayedPersonBonusHeaderDto> bonuses = new ArrayList<>();
}
