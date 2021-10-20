package com.fungisearch.fudriver.warehouseEastMushrooms.command.repository;

import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.reclassification.ReclassificationDetail;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.reclassification.ReclassificationHeader;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePalette;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehouseUnit;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.wz.ShipmentOrder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class EastMushroomsWarehouseRepository {

    @PersistenceContext
    private EntityManager em;


    public void save(WarehousePalette warehousePalette){
        em.persist(warehousePalette);
    }

    public void save(WarehouseUnit warehouseUnit) {
        em.persist(warehouseUnit);
    }

    public WarehousePalette findWarehousePalette(Long paletteId) {
        return em.find(WarehousePalette.class,paletteId);
    }

    public WarehousePalette findWarehousePaletteByHarvestPaletteId(long harvestPaletteId) {
        Query query = em.createQuery("select w.warehousePalette from WarehouseUnit w where w.harvestPaletteId = :harvestPaletteId and w.status = 0");
        query.setParameter("harvestPaletteId",harvestPaletteId);
        WarehousePalette result = null;
        List<WarehousePalette> palettes = query.getResultList();
        if(!palettes.isEmpty()){
            result = palettes.get(0);
        }
        return result;
    }

    public void removeUnit(WarehouseUnit warehouseUnit) {
        em.remove(warehouseUnit);
    }

    public void save(ReclassificationHeader reclassificationHeader) {
        em.persist(reclassificationHeader);
    }

    public void save(ReclassificationDetail reclassificationDetail) {
        em.persist(reclassificationDetail);
    }

    public WarehouseUnit findWarehouseUnit(long id) {
        return em.find(WarehouseUnit.class,id);
    }

    public void remove(WarehousePalette warehousePalette) {
        em.remove(warehousePalette);
    }

    public void save(ShipmentOrder shipmentOrder) {
        em.persist(shipmentOrder);
    }
}
