package com.fungisearch.fudriver.payroll.wage.command.repository;

import com.fungisearch.fudriver.payroll.wage.command.model.Wage;
import com.fungisearch.fudriver.payroll.wage.command.model.WageHeader;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Queue;


@Repository
public class WageHibernateRepository implements WageRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Wage wage) {
        em.persist(wage);
    }


    @Override
    public void save(WageHeader wageHeader) {
        em.persist(wageHeader);
    }

    @Override
    public List<WageHeader> getAllHeaders() {
        return em.createQuery("select wh from WageHeader wh").getResultList();

    }

    @Override
    public Wage findWage(long id) {
        return em.find(Wage.class, id);
    }

    @Override
    public WageHeader findHeader(long id) {
        return em.find(WageHeader.class,id);
    }




}
