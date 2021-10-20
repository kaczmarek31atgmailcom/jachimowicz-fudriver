package com.fungisearch.fudriver.box.command.model;

import com.fungisearch.fudriver.box.command.repository.BoxRepository;
import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;

@Entity
@Table(name = "skrz_rodzaj")
public class Box extends BaseEntity{

    public transient BoxRepository boxRepository;
    public transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private boolean active;

    private Box(){}

    public Box(BoxRepository boxRepository, BeanValidator beanValidator) {
        this.boxRepository = boxRepository;
        this.beanValidator = beanValidator;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        beanValidator.validate(this);
    }

    public boolean isActive() {
        return active;
    }

    private void create(){
        beanValidator.validate(this);
        boxRepository.create(this);
    }

    public void delete(){
        this.active = false;
        beanValidator.validate(this);
    }

    public static class BoxBuilder{
        private BoxRepository boxRepository;
        private BeanValidator beanValidator;
        private String name;

        public BoxBuilder(BoxRepository boxRepository, BeanValidator beanValidator) {
            this.boxRepository = boxRepository;
            this.beanValidator = beanValidator;
        }

        public BoxBuilder name(String name){
            this.name = name;
            return this;
        }

        public Box build(){
            Box box = new Box(boxRepository,beanValidator);
            box.name = name;
            box.active = true;
            box.create();
            return box;
        }
    }
}
