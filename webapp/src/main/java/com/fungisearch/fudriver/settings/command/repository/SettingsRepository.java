package com.fungisearch.fudriver.settings.command.repository;

import com.fungisearch.fudriver.settings.command.model.Company;
import com.fungisearch.fudriver.settings.command.model.LocalReclassifyReason;

/**
 * Created by marcin on 02.08.16.
 */
public interface SettingsRepository {
    void saveLocalReclassifyReason(LocalReclassifyReason reclassifyReason);
    LocalReclassifyReason findReason(Long id);
    Company getCompany(long companyId);
    void saveCompany(Company company);
}
