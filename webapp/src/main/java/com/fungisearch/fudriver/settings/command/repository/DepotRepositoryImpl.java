package com.fungisearch.fudriver.settings.command.repository;

import com.fungisearch.fudriver.settings.command.model.Depot;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class DepotRepositoryImpl implements DepotRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Depot depot) {
        em.persist(depot);
    }

    @Override
    public Depot find(long id) {
        return em.find(Depot.class,id);
    }
}
