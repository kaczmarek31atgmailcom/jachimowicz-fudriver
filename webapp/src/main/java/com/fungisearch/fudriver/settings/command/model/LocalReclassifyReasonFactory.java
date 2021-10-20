package com.fungisearch.fudriver.settings.command.model;

import com.fungisearch.fudriver.settings.command.repository.SettingsRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by marcin on 02.08.16.
 */
@Service
public class LocalReclassifyReasonFactory {

    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private BeanValidator beanValidator;

    public LocalReclassifyReason.LocalReclassifyReasonBuilder getBuilder(){
        LocalReclassifyReason.LocalReclassifyReasonBuilder builder = new LocalReclassifyReason.LocalReclassifyReasonBuilder(settingsRepository,beanValidator);
        return builder;
    }

    public LocalReclassifyReason findReason(Long id){
        LocalReclassifyReason reclassifyReason = settingsRepository.findReason(id);
        if(reclassifyReason != null){
            reclassifyReason.setSettingsRepository(this.settingsRepository);
            reclassifyReason.setBeanValidator(this.beanValidator);
        }
    return reclassifyReason;
    }
}
