package com.fungisearch.fudriver.settings.command.repository;

import com.fungisearch.fudriver.settings.command.model.Subsoil;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class SubsoilRepositoryImpl implements SubsoilRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Subsoil subsoil) {
        em.persist(subsoil);
    }

    @Override
    public Subsoil find(long id) {
        return em.find(Subsoil.class,id);
    }
}
