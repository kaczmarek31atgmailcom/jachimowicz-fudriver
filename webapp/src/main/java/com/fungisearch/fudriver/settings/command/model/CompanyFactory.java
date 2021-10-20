package com.fungisearch.fudriver.settings.command.model;

import com.fungisearch.fudriver.settings.command.repository.SettingsRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by marcin on 14.03.17.
 */
@Service
public class CompanyFactory {

    private final SettingsRepository settingsRepository;
    private final BeanValidator beanValidator;

    @Autowired
    public CompanyFactory(SettingsRepository settingsRepository, BeanValidator beanValidator){
        this.settingsRepository = settingsRepository;
        this.beanValidator = beanValidator;
    }

    public Company.CompanyBuilder getBuilder(){
        return new Company.CompanyBuilder(settingsRepository,beanValidator);
    }

    public Company getCompany(long companyId){
        Company company  = settingsRepository.getCompany(companyId);
        if(company != null) {
            company.settingsRepository = this.settingsRepository;
            company.beanValidator = this.beanValidator;
        }
        return company;
    }
}
