package com.fungisearch.fudriver.cycle.command.repository;

import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.settings.command.model.Chamber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class CycleRepositoryImpl implements CycleRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Cycle cycle) {
        em.persist(cycle);
    }

    @Override
    public Cycle find(long id) {
        return em.find(Cycle.class,id);
    }

    @Override
    public Cycle findOpenCycleByChamberId(long chamberId) {
        Query query = em.createQuery("select c from Cycle c where c.chamber.id = :chamberId and c.end is null");
        query.setParameter(new String("chamberId"), chamberId);
        List<Cycle> resultList = query.getResultList();
        if(resultList.isEmpty()){
            return null;
        }
        else {
            return resultList.get(0);
        }
    }
}
