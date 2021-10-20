package com.fungisearch.fudriver.payroll.salary.command.repository;

import com.fungisearch.fudriver.payroll.salary.command.model.PayrollMonth;
import com.fungisearch.fudriver.payroll.salary.command.model.bonus.BonusPersonMonthAssignment;
import com.fungisearch.fudriver.payroll.salary.command.model.bonus.PayrollBonus;
import com.fungisearch.fudriver.payroll.salary.command.model.bonus.PayrollFixedBonus;
import com.fungisearch.fudriver.payroll.salary.command.model.bonus.PayrollPercentageBonus;
import com.fungisearch.fudriver.person.person.command.model.Person;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PayrollBonusRepositoryImpl implements PayrollBonusRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(PayrollFixedBonus payrollFixedBonus) {
        em.persist(payrollFixedBonus);
    }

    @Override
    public void save(PayrollPercentageBonus payrollPercentageBonus) {
        em.persist(payrollPercentageBonus);
    }

    @Override
    public void save(BonusPersonMonthAssignment bonusPersonMonthAssignement) {
        em.persist(bonusPersonMonthAssignement);
    }

    @Override
    public List<BonusPersonMonthAssignment> findAssignedBonusesInNotPayedMonths(PayrollBonus bonus) {
        Query query = em.createQuery("select bpma from BonusPersonMonthAssignment  bpma where bpma.payrollMonth.isClosed = false and bpma.payrollBonus = :bonus");
        query.setParameter("bonus",bonus);
        List<BonusPersonMonthAssignment> result = new ArrayList<>();
        result.addAll(query.getResultList());
        return result;
    }

    @Override
    public PayrollBonus findBonus(long id) {
        return em.find(PayrollBonus.class,id);

    }

    @Override
    public BonusPersonMonthAssignment findBonusPersonMonthAssignment(Person person, PayrollBonus bonus, PayrollMonth month) {
        Query query = em.createQuery("select a from BonusPersonMonthAssignment  a where a.payrollBonus = :bonus and a.payrollMonth = :month and a.person = :person");
        query.setParameter("bonus", bonus);
        query.setParameter("month", month);
        query.setParameter("person", person);
        List<BonusPersonMonthAssignment> theList = query.getResultList();
        BonusPersonMonthAssignment result = null;
        if(!(theList.isEmpty())){
            result = theList.get(0);
        }
        return result;
    }

    @Override
    public void delete(BonusPersonMonthAssignment bonusPersonMonthAssignment) {
        em.remove(bonusPersonMonthAssignment);
    }

    @Override
    public List<PayrollBonus> findAssignedBonusesForPersonInMonth(Person person, PayrollMonth month) {
        Query query = em.createQuery("select m.payrollBonus from BonusPersonMonthAssignment m where m.person = :person and m.payrollMonth = :month");
        query.setParameter("person", person);
        query.setParameter("month",month);
        return query.getResultList();
    }
}
