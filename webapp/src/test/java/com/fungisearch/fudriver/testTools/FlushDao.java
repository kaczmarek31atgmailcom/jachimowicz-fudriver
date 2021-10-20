package com.fungisearch.fudriver.testTools;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by marcin on 15.01.17.
 */
@Repository
public class FlushDao {

    @PersistenceContext
    private EntityManager em;

    public void flush(){
        em.flush();
    }
}
