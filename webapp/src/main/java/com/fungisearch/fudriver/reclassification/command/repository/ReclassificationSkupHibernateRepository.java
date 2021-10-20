package com.fungisearch.fudriver.reclassification.command.repository;

import com.fungisearch.fudriver.reclassification.command.model.ReclassificationSkup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * Created by marcin on 03.02.16.
 */
@Repository
public class ReclassificationSkupHibernateRepository implements ReclassificationSkupRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Long save(ReclassificationSkup reclassificationSkup) {
        if (reclassificationSkup.getId() != null) {
            throw new IllegalStateException("Object already exists. It should be persisted with update not save method");
        }
        Session session = sessionFactory.openSession();
        Serializable id = session.save(reclassificationSkup);
        session.flush();
        session.close();
        return (Long)id;
    }
}
