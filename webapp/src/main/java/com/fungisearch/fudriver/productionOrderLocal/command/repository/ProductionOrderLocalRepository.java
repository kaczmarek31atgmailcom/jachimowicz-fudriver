package com.fungisearch.fudriver.productionOrderLocal.command.repository;

import com.fungisearch.fudriver.productionOrderLocal.command.model.ProductionOrderLocal;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

@Repository
public class ProductionOrderLocalRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(ProductionOrderLocal order){
        em.persist(order);
    }

    public ProductionOrderLocal findByTypeIdAndDay(int typeId, Date day){
        Query query = em.createQuery("select o from ProductionOrderLocal o where o.typeId =:typeId and o.dueDate =:day");
        query.setParameter("typeId",typeId);
        query.setParameter("day",day);
        List<ProductionOrderLocal> list = query.getResultList();
        if(!(list.isEmpty())){
            return list.get(0);
        }
        return null;
    }
}
