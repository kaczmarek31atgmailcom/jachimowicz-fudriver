package com.fungisearch.fudriver.payroll.salary.command.repository;


import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.salary.command.model.*;
import com.fungisearch.fudriver.payroll.wage.command.model.Wage;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.type.command.model.Type;

public interface PayrollPayoffRepository {
    void save(HarvestSalary harvestSalary);

    void save(MonthlyPayoffDetail monthlyPayoffDetail);

    void save(MonthlyPayoffHeader monthlyPayoffHeader);

    HarvestSalary findHarvestSalary(Type type, Wage wage, SalaryDayTypeEnum dayType, PayrollMonth month);

    void delete(HarvestSalary harvestSalary);

    WorkTimeSalary findWorkTimeSalary(Person person, PayrollMonth payrollMonth);

    void save(WorkTimeSalary workTimeSalary);

    void save(BonusSalary bonusSalary);

}
