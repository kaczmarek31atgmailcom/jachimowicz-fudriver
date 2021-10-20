package com.fungisearch.fudriver.paletteType.command.repository;

import com.fungisearch.fudriver.paletteType.command.model.PaletteType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class PaletteTypeRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(PaletteType paletteType) {
        em.persist(paletteType);
    }

    public PaletteType find(int id) {
        return em.find(PaletteType.class, id);
    }

    public PaletteType findByRemotePaletteId(int remotePaletteId) {
        Query query = em.createQuery("select p from PaletteType p where p.remotePaletteId = :remotePaletteId");
        query.setParameter("remotePaletteId", remotePaletteId);
        List<PaletteType> palettes = query.getResultList();
        if (!palettes.isEmpty()) {
            return palettes.get(0);
        }
        return null;
    }
}
