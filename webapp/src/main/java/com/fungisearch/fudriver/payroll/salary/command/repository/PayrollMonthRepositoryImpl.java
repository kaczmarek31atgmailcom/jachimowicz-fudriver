package com.fungisearch.fudriver.payroll.salary.command.repository;

import com.fungisearch.fudriver.payroll.salary.command.model.PayrollMonth;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.util.Date;

@Repository
public class PayrollMonthRepositoryImpl implements PayrollMonthRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(PayrollMonth payrollMonth) {
        em.persist(payrollMonth);
    }

    @Override
    public PayrollMonth findByDay(Date day) {
        Date firstDayOfTheMonth = new DateTime(day).withDayOfMonth(1).toDate();
        Query query = em.createQuery("select m from PayrollMonth m where m.firstDay = :firstDayOfTheMonth");
        query.setParameter("firstDayOfTheMonth", firstDayOfTheMonth, TemporalType.DATE);
        return (PayrollMonth) query.getSingleResult();
    }

    @Override
    public PayrollMonth find(long bonusId) {
        return em.find(PayrollMonth.class,bonusId);
    }
}
