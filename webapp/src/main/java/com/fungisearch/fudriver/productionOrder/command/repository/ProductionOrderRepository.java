package com.fungisearch.fudriver.productionOrder.command.repository;

import com.fungisearch.fudriver.productionOrder.command.model.ProductionOrder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ProductionOrderRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(ProductionOrder order){
        em.persist(order);
    }

    public ProductionOrder findByWarehouseId(int warehouseOrderId) {
        Query query = em.createQuery("select p from ProductionOrder p where p.warehouseOrderId =:warehouseOrderId");
        query.setParameter("warehouseOrderId" ,warehouseOrderId);
        List<ProductionOrder> productionOrderList = query.getResultList();
        if(! productionOrderList.isEmpty()){
            return productionOrderList.get(0);
        }
        return null;
    }
}
