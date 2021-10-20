package com.fungisearch.fudriver.settings.command.model;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.settings.command.repository.ChamberRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by marcin on 08.04.17.
 */
@Entity
@Table(name = "hala")
public class Chamber extends BaseEntity {

    transient ChamberRepository chamberRepository;
    transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "powierzchnia")
    @NotNull
    private int area;

    @Column(name = "aktywna")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "chlodnia_id", nullable = false)
    private Depot depot;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    private Chamber() {
    }

    public Chamber(ChamberRepository chamberRepository, BeanValidator beanValidator) {
        this.chamberRepository = chamberRepository;
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

    public int getArea() {
        return area;
    }

    public boolean isActive() {
        return isActive;
    }

    public Depot getDepot() {
        return depot;
    }

    public Company getCompany(){
        return company;
    }

    private void create() {
        beanValidator.validate(this);
        chamberRepository.save(this);
    }

    public void inactivate(){
        this.isActive = false;
        beanValidator.validate(this);
    }

    public void edit(Edit edit){
        if(edit.area != null){
            this.area = edit.area;
        }
        if(edit.name != null){
            this.name = edit.name;
        }

        if(edit.depot != null){
            this.depot.getChambers().remove(this);
            this.depot = edit.depot;
            this.depot.getChambers().add(this);
        }

        if(edit.company != null){
            this.company = edit.company;
        }
        beanValidator.validate(this);
    }

    public static class Edit{
        private Integer area;
        private String name;
        private Depot depot;
        private Company company;

        public Edit area(int area){
            this.area = area;
            return this;
        }

        public Edit name(String name){
            this.name = name;
            return this;
        }

        public Edit depot(Depot depot){
            this.depot = depot;
            return this;
        }

        public Edit company(Company company){
            this.company = company;
            return this;
        }
    }

    public static class ChamberBuilder {
        private ChamberRepository chamberRepository;
        private BeanValidator beanValidator;
        private String name;
        private int area;
        private Depot depot;
        private Company company;

        public ChamberBuilder(ChamberRepository chamberRepository, BeanValidator beanValidator) {
            this.chamberRepository = chamberRepository;
            this.beanValidator = beanValidator;
        }

        public ChamberBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ChamberBuilder area(int area) {
            this.area = area;
            return this;
        }

        public ChamberBuilder depot(Depot depot) {
            this.depot = depot;
            return this;
        }

        public ChamberBuilder company(Company company){
            this.company = company;
            return this;
        }

        public Chamber build() {
            Chamber chamber = new Chamber(chamberRepository, beanValidator);
            chamber.name = name;
            chamber.area = area;
            chamber.depot = depot;
            chamber.company = company;
            chamber.isActive = true;
            chamber.create();
            depot.getChambers().add(chamber);
            return chamber;
        }
    }
}
