package com.fungisearch.fudriver.settings.command.model;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.settings.command.repository.SubsoilRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;

@Entity
@Table(name = "kompostownia")
public class Subsoil extends BaseEntity {

    transient SubsoilRepository subsoilRepository;
    transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "nazwa")
    private String name;

    @Column(name = "active")
    private boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    private Subsoil() {
    }

    public Subsoil(SubsoilRepository subsoilRepository, BeanValidator beanValidator) {
        this.subsoilRepository = subsoilRepository;
        this.beanValidator = beanValidator;
    }

    private void create() {
        beanValidator.validate(this);
        subsoilRepository.save(this);
    }

    public void inactivate() {
        this.isActive = false;
        beanValidator.validate(this);
    }

    public void changeName(ChangeName changeName) {
        this.name = changeName.name;
        beanValidator.validate(this);
    }

    public static class ChangeName {
        private String name;

        public ChangeName name(String name) {
            this.name = name;
            return this;
        }
    }

    public static class SubsoilBuilder {
        private SubsoilRepository subsoilRepository;
        private BeanValidator beanValidator;
        private String name;

        public SubsoilBuilder(SubsoilRepository subsoilRepository, BeanValidator beanValidator) {
            this.subsoilRepository = subsoilRepository;
            this.beanValidator = beanValidator;
        }

        public SubsoilBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Subsoil build() {
            Subsoil subsoil = new Subsoil(subsoilRepository, beanValidator);
            subsoil.name = name;
            subsoil.isActive = true;
            subsoil.create();
            return subsoil;
        }
    }
}
