package com.fungisearch.fudriver.settings.command.model;

import com.fungisearch.fudriver.settings.command.repository.SettingsRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by marcin on 02.08.16.
 */
@Entity
@Table(name="reclassify_reason")
public class LocalReclassifyReason {

    private transient SettingsRepository settingsRepository;
    private transient BeanValidator beanValidator;


    @Id
    @GeneratedValue
    private Long id;

    @Column(name="description")
    @NotNull
    private String description;

    @Column(name="active")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean active;

    private LocalReclassifyReason(){}

    public LocalReclassifyReason(SettingsRepository settingsRepository, BeanValidator beanValidator){
        this.settingsRepository = settingsRepository;
        this.beanValidator = beanValidator;
    }

    public void setSettingsRepository(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public void setBeanValidator(BeanValidator beanValidator) {
        this.beanValidator = beanValidator;
    }

    public String getDescription() {
        return description;
    }

    public Long create(){
        this.active = true;
        beanValidator.validate(this);
        settingsRepository.saveLocalReclassifyReason(this);
        return this.id;
    }

    public void remove(){
        this.active = false;
        beanValidator.validate(this);
        settingsRepository.saveLocalReclassifyReason(this);
    }

    public static class LocalReclassifyReasonBuilder{
        private SettingsRepository settingsRepository;
        private BeanValidator beanValidator;
        private String description;

        public LocalReclassifyReasonBuilder(SettingsRepository settingsRepository, BeanValidator beanValidator){
            this.settingsRepository = settingsRepository;
            this.beanValidator = beanValidator;
        }

        public LocalReclassifyReasonBuilder description(String description){
            this.description = description;
            return this;
        }

        public LocalReclassifyReason build(){
            LocalReclassifyReason localReclassifyReason = new LocalReclassifyReason(this.settingsRepository,this.beanValidator);
            localReclassifyReason.description = this.description;
            return localReclassifyReason;
        }
    }
}
