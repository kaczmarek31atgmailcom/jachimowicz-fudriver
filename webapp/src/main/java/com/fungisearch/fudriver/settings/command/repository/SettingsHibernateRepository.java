package com.fungisearch.fudriver.settings.command.repository;

import com.fungisearch.fudriver.settings.command.model.Company;
import com.fungisearch.fudriver.settings.command.model.LocalReclassifyReason;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by marcin on 02.08.16.
 */
@Repository
public class SettingsHibernateRepository implements SettingsRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void saveLocalReclassifyReason(LocalReclassifyReason reclassifyReason) {
        em.persist(reclassifyReason);
        em.flush();
    }

    @Override
    public LocalReclassifyReason findReason(Long id) {
        return em.find(LocalReclassifyReason.class, id);
    }

    @Override
    public Company getCompany(long companyId) {
        return em.find(Company.class, companyId);
    }

    @Override
    public void saveCompany(Company company) {
        em.persist(company);
    }
}
