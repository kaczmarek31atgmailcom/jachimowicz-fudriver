package com.fungisearch.fudriver.wozek.command.repository;

import com.fungisearch.fudriver.wozek.command.model.Uniq;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by marcin on 23.02.16.
 */
@Repository
public class UniqHibernateRepository implements UniqRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public void save(Uniq uniq){
        em.persist(uniq);
    }

    @Override
    public void delete(Uniq uniq) {
        em.remove(uniq);
        em.flush();
    }

    @Override
    public List<Uniq> findOpenForPicker(Long pickerId) {
        Query query = em.createQuery("select u from Uniq u where u.pickerId = :pickerId and u.used = false ");
        query.setParameter(new String("pickerId"), pickerId);
        return query.getResultList();
    }

    @Override
    public Uniq findTransactional(Long pickerId, Long uniqId) {
        javax.persistence.Query query = em.createQuery("select u from Uniq u where u.pickerId =:pickerId and u.uniqId =:uniqId");
        query.setParameter(new String("pickerId"), pickerId);
        query.setParameter(new String("uniqId"), uniqId);
        List<Uniq> resultList = query.getResultList();
        if(resultList.isEmpty()){
            return null;
        } else {
            return resultList.get(0);
        }
    }


    @Override
    public Long findHighestUniqIdForPicker(Long pickerId) {
        Query query = em.createQuery("select max(uniqId) from Uniq where pickerId =:pickerId");
        query.setParameter("pickerId", pickerId);
        Long id = (Long)query.getResultList().get(0);
        if(id == null){
            id = 0L;
        }
        return id;
    }


}
