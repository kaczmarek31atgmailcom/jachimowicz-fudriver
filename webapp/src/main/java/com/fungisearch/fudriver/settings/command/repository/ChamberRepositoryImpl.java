package com.fungisearch.fudriver.settings.command.repository;

import com.fungisearch.fudriver.settings.command.model.Chamber;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ChamberRepositoryImpl implements ChamberRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Chamber chamber) {
        em.persist(chamber);
    }

    @Override
    public Chamber find(long id) {
        return em.find(Chamber.class,id);
    }
}
