package com.fungisearch.fudriver.timeRecorder.command.repository;

import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLog;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created by marcin on 06.04.16.
 */
@Service
public class TimeWorkLogHibernateRepository implements TimeWorkLogRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public TimeWorkLog findOpen(Long personId) {
        TimeWorkLog result = null;
        Query query = em.createQuery("select twl from TimeWorkLog twl where twl.isOpened = true and twl.person.id = :personId");
        query.setParameter(new String("personId"), personId);
        List<TimeWorkLog> list = query.getResultList();
        if (!list.isEmpty()) {
            result = list.get(0);
        }
        return result;
    }

    @Override
    public void save(TimeWorkLog timeWorkLog) {
        em.persist(timeWorkLog);
    }

    @Override
    public TimeWorkLog find(Long id) {
        return em.find(TimeWorkLog.class, id);
    }

    @Override
    public void delete(TimeWorkLog timeWorkLog) {
        em.remove(timeWorkLog);
    }

    @Override
    public List<TimeWorkLog> findAllOpened() {
        Query query = em.createQuery("select twl from TimeWorkLog twl where twl.isOpened = true");
        return query.getResultList();
    }

    @Override
    public List<TimeWorkLog> findForPeriod(Date firstDay, Date lastDay,Person person) {
        Query query = em.createQuery("select twl from TimeWorkLog twl where twl.person = :person and (twl.startTime between :firstDay and :lastDay or twl.endTime between :firstDay and :lastDay)");
        query.setParameter("firstDay", firstDay);
        query.setParameter("lastDay", lastDay);
        query.setParameter("person", person);
        return query.getResultList();
    }

    @Override
    public List<Person> findPeopleInPeriod(Date firstDay, Date lastDay) {
        Query query = em.createQuery("select distinct(twl.person) from TimeWorkLog twl where twl.startTime between :firstDay and :lastDay or twl.endTime between :firstDay and :lastDay");
        query.setParameter("firstDay", firstDay);
        query.setParameter("lastDay", lastDay);
        return query.getResultList();
    }

    @Override
    public List<TimeWorkLog> findAllInPeriod(Date firstDay, Date lastDay) {
        Query query = em.createQuery("select twl from TimeWorkLog twl where twl.startTime between :firstDay and :lastDay or twl.endTime between :firstDay and :lastDay");
        query.setParameter("firstDay", firstDay);
        query.setParameter("lastDay", lastDay);
        return query.getResultList();
    }
}
