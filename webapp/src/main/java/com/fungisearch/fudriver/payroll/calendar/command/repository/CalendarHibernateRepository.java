package com.fungisearch.fudriver.payroll.calendar.command.repository;

import com.fungisearch.fudriver.payroll.calendar.command.model.Calendar;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created by marcin on 13.05.16.
 */
@Repository
public class CalendarHibernateRepository implements CalendarRepository {

    @PersistenceContext
    private EntityManager em;


    @Override
    public List<Calendar> findBetweenDates(Date startDate, Date endDate) {
        Query query = em.createQuery("SELECT c FROM Calendar c where c.date between :startDate and :endDate");
        query.setParameter(new String("startDate"),startDate);
        query.setParameter(new String("endDate"), endDate);
        List<Calendar> result =query.getResultList();
        return result;
    }

    @Override
    public void create(Calendar calendar) {
        em.persist(calendar);
    }

    @Override
    public Calendar findByDate(Date date) {
        Query query = em.createQuery("select c from Calendar c where c.date = :date");
        query.setParameter(new String("date"), date);
        List<Calendar> calendars = query.getResultList();
        if(!calendars.isEmpty()) {
            return calendars.get(0);
        }
        else {
            return null;
        }
    }

    @Override
    public void update(Calendar calendar) {
        em.persist(calendar);
    }
}
