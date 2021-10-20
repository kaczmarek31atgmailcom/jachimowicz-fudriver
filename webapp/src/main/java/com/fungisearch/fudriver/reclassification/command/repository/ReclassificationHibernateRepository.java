package com.fungisearch.fudriver.reclassification.command.repository;

import com.fungisearch.fudriver.reclassification.command.model.ReclassificationDetailSkup;
import com.fungisearch.fudriver.reclassification.command.model.ReclassificationSkup;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiEntry;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by marcin on 17.02.16.
 */
@Repository
public class ReclassificationHibernateRepository implements ReclassificationRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<ReclassificationDetailSkup> getReclassificationDetails(Long id) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from ReclassificationDetailSkup as detail where detail.reclassificationId=:id");
        query.setParameter("id", id);
        List<ReclassificationDetailSkup> details = query.list();
        session.close();
        return details;
    }

    @Override
    public List<ReclassificationDetailSkup> getActiveReclassificationDetails(Long id) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from ReclassificationDetailSkup as detail where detail.reclassificationId=:id and detail.active=:true");
        query.setParameter("id", id);
        query.setParameter("true", true);
        List<ReclassificationDetailSkup> details = query.list();
        session.close();
        return details;
    }



    @Override
    public ZarobkiEntry findZarobki(Long pickerId, Long uniqId) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from ZarobkiEntry as zarobki where zarobki.uniqId=:uniqId and zarobki.pickerId=:pickerId");
        query.setParameter("pickerId", pickerId);
        query.setParameter("uniqId", uniqId);
        ZarobkiEntry zarobkiEntry = (ZarobkiEntry)query.uniqueResult();
        session.close();
        return zarobkiEntry;
    }

    @Override
    public void updateZarobkiEntry(ZarobkiEntry zarobkiEntry) {
        Session session = sessionFactory.openSession();
        session.update(zarobkiEntry);
        session.flush();
        session.close();
    }

    @Override
    public ReclassificationSkup findReclassification(Long id) {
        Session session = sessionFactory.openSession();
        ReclassificationSkup reclassificationSkup = (ReclassificationSkup)session.get(ReclassificationSkup.class, id);
        session.close();
        return reclassificationSkup;
    }

    @Override
    public void updateReclassificationSkup(ReclassificationSkup reclassificationSkup) {
        Session session = sessionFactory.openSession();
        session.update(reclassificationSkup);
        session.flush();
        session.close();
    }
}
