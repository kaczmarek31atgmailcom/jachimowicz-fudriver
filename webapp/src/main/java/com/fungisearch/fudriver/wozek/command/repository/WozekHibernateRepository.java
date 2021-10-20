package com.fungisearch.fudriver.wozek.command.repository;

import com.fungisearch.fudriver.wozek.command.model.WozekEntry;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by marcin on 23.02.16.
 */
@Repository
public class WozekHibernateRepository implements WozekRepository {


    @PersistenceContext
    EntityManager em;

    @Override
    public void save(WozekEntry wozekEntry) {
        em.persist(wozekEntry);
    }

    @Override
    public WozekEntry findOne(Long id) {
        WozekEntry wozekEntry = em.find(WozekEntry.class,id);
        return wozekEntry;
    }

    @Override
    public void delete(WozekEntry wozekEntry) {
        em.remove(wozekEntry);
        em.flush();
    }

    @Override
    public List<WozekEntry> getEntriesForWozekId(Long wozekId) {
        Query query  = em.createQuery(" select w from WozekEntry w where w.wozekNr=:wozekNr");
        query.setParameter(new String("wozekNr"), wozekId);
        List<WozekEntry> wozekEntries = query.getResultList();
        return wozekEntries;
    }

    @Override
    public Long getTotalAmount(Long nr) {
        javax.persistence.Query query  = em.createQuery("select count(w) from WozekEntry w where w.wozekNr =:nr");
        query.setParameter(new String("nr"), nr);
        return (Long) query.getSingleResult();
    }

    @Override
    public WozekEntry findByPickerAndUniq(Long pickerId, Long uniqId) {
        Query query = em.createQuery("select w from WozekEntry w where w.pickerId =:pickerId and w.uniqId =:uniqId");
        query.setParameter("pickerId",pickerId);
        query.setParameter("uniqId",uniqId);
        List<WozekEntry> resultList = query.getResultList();
        WozekEntry wozekEntry =  null;
        if(!resultList.isEmpty()){
            wozekEntry = resultList.get(0);
        }
        return wozekEntry;
    }
}
