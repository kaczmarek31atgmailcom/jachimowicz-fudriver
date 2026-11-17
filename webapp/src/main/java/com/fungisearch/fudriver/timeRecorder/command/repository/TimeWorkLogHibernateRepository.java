package com.fungisearch.fudriver.timeRecorder.command.repository;

import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLog;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
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
        // Lock the employee even when there is no work log yet. The caller must
        // keep this transaction open until the new log has been persisted.
        lockPerson(personId);
        TimeWorkLog result = null;
        Query query = em.createQuery("select twl from TimeWorkLog twl where twl.isOpened = true and twl.person.id = :personId");
        query.setParameter("personId", personId);
        // A locking read sees the latest committed row under MySQL REPEATABLE READ.
        query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        List<TimeWorkLog> list = query.getResultList();
        if (!list.isEmpty()) {
            result = list.get(0);
        }
        return result;
    }

    @Override
    public void lockPerson(Long personId) {
        em.find(Person.class, personId, LockModeType.PESSIMISTIC_WRITE);
    }

    @Override
    public boolean hasOverlap(Long personId, Long excludedId, Date start, Date end) {
        // Caller holds the employee lock; use a current read, not a repeatable-read snapshot.
        String jpql = "select t from TimeWorkLog t where t.person.id = :personId "
                + "and (t.isOpened = true or t.endTime > :start) "
                + (end == null ? "" : "and t.startTime < :end ")
                + (excludedId == null ? "" : "and t.id <> :excludedId ");
        Query query = em.createQuery(jpql).setParameter("personId", personId)
                .setParameter("start", start).setLockMode(LockModeType.PESSIMISTIC_WRITE);
        if (end != null) query.setParameter("end", end);
        if (excludedId != null) query.setParameter("excludedId", excludedId);
        return !query.setMaxResults(1).getResultList().isEmpty();
    }

    @Override
    public List<TimeWorkLog> findOpenedBefore(Long personId, Date dayStart) {
        return em.createQuery("select t from TimeWorkLog t where t.person.id=:personId "
                + "and t.isOpened=true and t.startTime<:dayStart order by t.startTime,t.id", TimeWorkLog.class)
                .setParameter("personId", personId).setParameter("dayStart", dayStart)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();
    }

    @Override
    public Date findNextStart(Long personId, Long excludedId, Date start, Date dayEnd) {
        List<TimeWorkLog> next = em.createQuery("select t from TimeWorkLog t where t.person.id=:personId "
                + "and t.id<>:excludedId and t.startTime>=:start and t.startTime<:dayEnd "
                + "order by t.startTime,t.id", TimeWorkLog.class)
                .setParameter("personId", personId).setParameter("excludedId", excludedId)
                .setParameter("start", start).setParameter("dayEnd", dayEnd)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE).setMaxResults(1).getResultList();
        return next.isEmpty() ? null : next.get(0).getStartTime();
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
