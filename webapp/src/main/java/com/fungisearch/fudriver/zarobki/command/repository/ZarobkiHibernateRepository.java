package com.fungisearch.fudriver.zarobki.command.repository;

import com.fungisearch.fudriver.zarobki.command.model.ZarobkiEntry;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by marcin on 18.03.16.
 */
@Repository
public class ZarobkiHibernateRepository implements ZarobkiRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public Long save(ZarobkiEntry zarobkiEntry) {
        em.persist(zarobkiEntry);
        return zarobkiEntry.id;
    }

    @Override
    public List<ZarobkiEntry> findWozek(Long wozekNr) {
        Query query = em.createQuery("select z from ZarobkiEntry z where z.trolleyId =:wozekNr");
        query.setParameter(new String("wozekNr"), wozekNr);
        List<ZarobkiEntry> wozek = query.getResultList();
        return wozek;
    }

    @Override
    public void delete(ZarobkiEntry zarobkiEntry) {
        em.remove(zarobkiEntry);
        em.flush();
    }

    @Override
    public ZarobkiEntry findById(Long id) {
        return em.find(ZarobkiEntry.class, id);
    }

    @Override
    public ZarobkiEntry findByPersonAndUniq(Long personId, Long uniqId) {
        Query query = em.createQuery("select z from ZarobkiEntry z where z.pickerId = :personId and z.uniqId = :uniqId");
        query.setParameter(new String("personId"), personId);
        query.setParameter(new String("uniqId"), uniqId);
        return (ZarobkiEntry) query.getSingleResult();
    }

    @Override
    public Date findMinDateForCycle(long cycleId) {
        Query query = em.createQuery("select z.harvestTime from ZarobkiEntry z where z.cycleId = :cycleId order by z.harvestTime desc");
        query.setParameter(new String("cycleId"), cycleId);
        query.setMaxResults(1);
        List<Date> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return null;
        } else {
            return resultList.get(0);
        }
    }

    @Override
    public Date findMaxDateForCycle(long cycleId) {
        Query query = em.createQuery("select z.harvestTime from ZarobkiEntry z where z.cycleId = :cycleId order by z.harvestTime asc");
        query.setParameter(new String("cycleId"), cycleId);
        query.setMaxResults(1);
        List<Date> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return null;
        } else {
            return resultList.get(0);
        }
    }

    @Override
    public List<ZarobkiEntry> findPersonZarobkiInMonth(long personId, long timeshort) {
        Query query = em.createQuery("select z from ZarobkiEntry  z where z.pickerId = :personId and z.timeshort = :timeshort and z.rodzajId > 0");
        query.setParameter("personId", personId);
        query.setParameter("timeshort", timeshort);
        return query.getResultList();
    }

    @Override
    public List<Long> getLudzieIdsInMonth(long timesort) {
        Query query = em.createQuery("select distinct(e.pickerId) from ZarobkiEntry e where e.timeshort = :timeshort and e.rodzajId > 0");
        query.setParameter("timeshort",timesort);
        return query.getResultList();
    }
}