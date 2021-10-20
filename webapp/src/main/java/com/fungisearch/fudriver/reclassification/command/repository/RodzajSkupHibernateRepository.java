package com.fungisearch.fudriver.reclassification.command.repository;

import com.fungisearch.fudriver.reclassification.command.model.RodzajSkup;
import com.fungisearch.fudriver.reclassification.command.model.RodzajSkupGrupa;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by marcin on 03.02.16.
 */
@Repository
public class RodzajSkupHibernateRepository implements RodzajSkupRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public RodzajSkup get(Long id) {
        return em.find(RodzajSkup.class, id);
    }

    @Override
    public List<RodzajSkup> getAllRodzajSkup() {
        Query query = em.createQuery("select r from RodzajSkup r");
        return query.getResultList();
    }


    @Override
    public void save(RodzajSkup rodzajSkup) {
        em.persist(rodzajSkup);
    }

    @Override
    public void save(RodzajSkupGrupa rodzajSkupGrupa) {
        em.persist(rodzajSkupGrupa);
    }

    @Override
    public RodzajSkupGrupa findByGroupIdRodzajSkupGrupa(long groupId) {
        Query query = em.createQuery("Select g from RodzajSkupGrupa g where g.groupId = :groupId");
        query.setParameter("groupId",groupId);
        List<RodzajSkupGrupa> list = query.getResultList();
        RodzajSkupGrupa result = null;
        if(!(list.isEmpty())){
            result = list.get(0);
        }
        return result;
    }

    @Override
    public void remove(RodzajSkup rodzajSkup) {
        em.remove(rodzajSkup);
    }

}
