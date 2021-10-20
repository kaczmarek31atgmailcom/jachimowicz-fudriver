package com.fungisearch.fudriver.box.command.repository;

import com.fungisearch.fudriver.box.command.model.Box;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Repository
public class BoxRepositoryImpl implements BoxRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Box box) {
        em.persist(box);
    }

    @Override
    public Box find(long id) {
        return em.find(Box.class, id);
    }
}
