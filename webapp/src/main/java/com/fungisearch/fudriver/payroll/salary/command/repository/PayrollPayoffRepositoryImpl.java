package com.fungisearch.fudriver.payroll.salary.command.repository;

import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.salary.command.model.*;
import com.fungisearch.fudriver.payroll.wage.command.model.Wage;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.type.command.model.Type;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Service
public class PayrollPayoffRepositoryImpl implements PayrollPayoffRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(HarvestSalary harvestSalary) {
        em.persist(harvestSalary);
    }

    @Override
    public void save(MonthlyPayoffDetail monthlyPayoffDetail) {
        em.persist(monthlyPayoffDetail);
    }

    @Override
    public void save(MonthlyPayoffHeader monthlyPayoffHeader) {
        em.persist(monthlyPayoffHeader);
    }

    @Override
    public HarvestSalary findHarvestSalary(Type type, Wage wage, SalaryDayTypeEnum dayType, PayrollMonth month) {
        Query query = em.createQuery("select hs from HarvestSalary  hs where hs.type = :type and hs.wage = :wage and hs.dayType = :dayType and hs.month = :month");
        query.setParameter("type", type);
        query.setParameter("wage", wage);
        query.setParameter("dayType", dayType);
        query.setParameter("month",month);
        HarvestSalary harvestSalary = null;
        if(!((query.getResultList().isEmpty()))){
            harvestSalary = (HarvestSalary)query.getResultList().get(0);
        }
        return harvestSalary;
    }

    @Override
    public void delete(HarvestSalary harvestSalary) {
        em.remove(harvestSalary);
    }

    @Override
    public WorkTimeSalary findWorkTimeSalary(Person person, PayrollMonth payrollMonth) {
        Query query = em.createQuery("select wts from WorkTimeSalary  wts where wts.person = :person and wts.month = :month");
        query.setParameter("person", person);
        query.setParameter("month", payrollMonth);
        WorkTimeSalary workTimeSalary = null;
        if(!(query.getResultList().isEmpty())){
            workTimeSalary = (WorkTimeSalary)query.getResultList().get(0);
        }
    return workTimeSalary;
    }

    @Override
    public void save(WorkTimeSalary workTimeSalary) {
        em.persist(workTimeSalary);
    }

    @Override
    public void save(BonusSalary bonusSalary) {
        em.persist(bonusSalary);
    }
}
