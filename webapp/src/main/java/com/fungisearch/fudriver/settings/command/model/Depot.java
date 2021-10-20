package com.fungisearch.fudriver.settings.command.model;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.settings.command.repository.DepotRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chlodnie")
public class Depot extends BaseEntity {

    transient DepotRepository depotRepository;
    transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    protected Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private boolean isActive;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "chlodnia_id")
    private Set<Chamber> chambers;


    private Depot() {
    }

    public Depot(DepotRepository depotRepository, BeanValidator beanValidator) {
        this.depotRepository = depotRepository;
        this.beanValidator = beanValidator;
    }

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

    public Set<Chamber> getChambers() {
        return chambers;
    }

    private void create() {
        beanValidator.validate(this);
        depotRepository.save(this);
    }

    public void deactivate() {
        this.isActive = false;
        beanValidator.validate(this);
    }

    public void edit(Edit edit){
        this.name = edit.name;
        beanValidator.validate(this);
    }

    public static class Edit{
        private String name;
        public Edit name (String name){
            this.name = name;
            return this;
        }
    }

    public static class DepotBuilder {
        private DepotRepository depotRepository;
        private BeanValidator beanValidator;
        private String name;

        public DepotBuilder(DepotRepository depotRepository, BeanValidator beanValidator) {
            this.depotRepository = depotRepository;
            this.beanValidator = beanValidator;
        }

        public DepotBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Depot build() {
            Depot depot = new Depot(depotRepository, beanValidator);
            depot.name = name;
            depot.isActive = true;
            depot.chambers = new HashSet();
            depot.create();
            return depot;
        }
    }
}
