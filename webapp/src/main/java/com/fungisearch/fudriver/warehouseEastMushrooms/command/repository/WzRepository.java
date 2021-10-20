package com.fungisearch.fudriver.warehouseEastMushrooms.command.repository;

import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.wz.Shipment;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.wz.WzDocument;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.wz.ShipmentPalette;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.wz.WzUnit;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class WzRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(WzUnit wzUnit) {
        em.persist(wzUnit);
    }

    public void save(ShipmentPalette wzPalette) {
        em.persist(wzPalette);
    }

    public void save(WzDocument wzDocument) {
        em.persist(wzDocument);
    }

    public void save(Shipment shipment) {
    em.persist(shipment);
    }
}
