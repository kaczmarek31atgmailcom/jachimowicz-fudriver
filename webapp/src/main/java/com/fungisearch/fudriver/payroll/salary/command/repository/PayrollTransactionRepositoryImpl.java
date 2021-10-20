package com.fungisearch.fudriver.payroll.salary.command.repository;

import com.fungisearch.fudriver.payroll.salary.command.model.PayrollTransaction;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PayrollTransactionRepositoryImpl implements PayrollTransactionRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(PayrollTransaction transaction) {
        em.persist(transaction);
    }
}
