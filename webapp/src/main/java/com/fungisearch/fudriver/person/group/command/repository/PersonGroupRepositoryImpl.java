package com.fungisearch.fudriver.person.group.command.repository;

import com.fungisearch.fudriver.person.group.command.model.PersonGroup;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by marcin on 04.01.17.
 */
@Repository
public class PersonGroupRepositoryImpl implements PersonGroupRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(PersonGroup personGroup) {
        em.persist(personGroup);
    }
}
