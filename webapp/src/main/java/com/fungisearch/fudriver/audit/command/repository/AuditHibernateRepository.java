package com.fungisearch.fudriver.audit.command.repository;

import com.fungisearch.fudriver.audit.command.model.LocalReclassificationLog;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by marcin on 03.08.16.
 */
@Repository
public class AuditHibernateRepository implements AuditRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void saveLocalReclassificationLog(LocalReclassificationLog log) {
        em.persist(log);
    }
}
