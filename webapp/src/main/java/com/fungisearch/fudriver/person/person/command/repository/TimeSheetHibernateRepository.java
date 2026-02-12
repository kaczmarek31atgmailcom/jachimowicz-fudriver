package com.fungisearch.fudriver.person.person.command.repository;

import com.fungisearch.fudriver.person.person.command.model.TimeSheet;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Created by marcin on 03.03.16.
 */
@Repository
public class TimeSheetHibernateRepository implements TimeSheetRepository {

@PersistenceContext
private EntityManager em;


    @Override
    public void addTimeSheet(TimeSheet timeSheet) {
        em.persist(timeSheet);
    }

    @Override
    public TimeSheet findLatestOne(Long personId) {
        Query query = em.createQuery("select t from  TimeSheet as t where t.personId = :personId order by t.id desc");
        query.setParameter(new String("personId"), personId);
        TimeSheet result = null;
        if(!query.getResultList().isEmpty()) {
            result = (TimeSheet) query.getResultList().get(0);
        }
        return result;
    }


    @Override
    public TimeSheet findOne(Long id) {
        return em.find(TimeSheet.class,id);
    }
}
