package com.fungisearch.fudriver.wozek.command.repository;

import com.fungisearch.fudriver.wozek.command.model.WozekSeq;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by idea on 15.03.16.
 */
@Repository
public class WozekSeqRepositoryImpl implements WozekSeqRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public Long create(WozekSeq wozekSeq) {
        em.persist(wozekSeq);
        em.flush();
        return wozekSeq.getId();
    }
}
