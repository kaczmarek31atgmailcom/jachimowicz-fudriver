package com.fungisearch.fudriver.reclassification.command.repository;

import com.fungisearch.fudriver.reclassification.command.model.ReclassificationDetailSkup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by marcin on 03.02.16.
 */
@Repository
public class ReclassificationDetailSkupHibernateRepository implements ReclassificationDetailSkupRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(ReclassificationDetailSkup detail) {
        if (detail.getId() != null) {
            throw new IllegalStateException("Object already exists. It should be persisted with update not save method");
        }
        Session session = sessionFactory.openSession();
        session.save(detail);
        session.flush();
        session.close();

    }

    @Override
    public ReclassificationDetailSkup find(Long id) {
        Session session = sessionFactory.openSession();
        ReclassificationDetailSkup reclassificationDetailSkup = (ReclassificationDetailSkup)session.get(ReclassificationDetailSkup.class, id);
        return reclassificationDetailSkup;
    }

    @Override
    public void update(ReclassificationDetailSkup detailSkup) {
        Session session = sessionFactory.openSession();
        session.update(detailSkup);
        session.flush();
        session.close();
    }
}
