package com.fungisearch.fudriver.wozek.command.repository;

import com.fungisearch.fudriver.wozek.command.model.TrolleyMan;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class TrolleyManRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(TrolleyMan trolleyMan){
        em.persist(trolleyMan);
    }

    public TrolleyMan find(int id) {
        return em.find(TrolleyMan.class,id);
    }
}
