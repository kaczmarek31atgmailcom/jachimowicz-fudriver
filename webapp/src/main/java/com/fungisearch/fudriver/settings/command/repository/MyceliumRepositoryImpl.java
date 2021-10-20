package com.fungisearch.fudriver.settings.command.repository;

import com.fungisearch.fudriver.settings.command.model.Mycelium;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MyceliumRepositoryImpl implements MyceliumRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Mycelium mycelium) {
        em.persist(mycelium);
    }

    @Override
    public Mycelium find(long id) {
        return em.find(Mycelium.class,id);
    }
}
