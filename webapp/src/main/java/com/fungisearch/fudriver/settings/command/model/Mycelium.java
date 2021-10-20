package com.fungisearch.fudriver.settings.command.model;


import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.settings.command.repository.MyceliumRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;

@Entity
@Table(name = "grzybnia")
public class Mycelium extends BaseEntity{

    transient MyceliumRepository myceliumRepository;
    transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    public Long id;

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

    private Mycelium(){}

    public Mycelium(MyceliumRepository myceliumRepository, BeanValidator beanValidator) {
        this.myceliumRepository = myceliumRepository;
        this.beanValidator = beanValidator;
    }

    private void create(){
        beanValidator.validate(this);
        myceliumRepository.save(this);
    }

    public void edit(Edit edit){
        this.name = edit.name;
        beanValidator.validate(this);
    }

    public void remove(){
        this.isActive = false;
        beanValidator.validate(this);
    }

    public static class Edit{
        private String name;

        public Edit name(String name){
            this.name = name;
            return this;
        }
    }

    public static class MyceliumBuilder{
        private MyceliumRepository mycelimRepository;
        private BeanValidator beanValidator;
        private String name;

        public MyceliumBuilder(MyceliumRepository mycelimRepository, BeanValidator beanValidator) {
            this.mycelimRepository = mycelimRepository;
            this.beanValidator = beanValidator;
        }

        public MyceliumBuilder name(String name){
            this.name = name;
            return this;
        }

        public Mycelium build(){
            Mycelium mycelium = new Mycelium(mycelimRepository,beanValidator);
            mycelium.name = name;
            mycelium.isActive = true;
            mycelium.create();
            return mycelium;
        }
    }
}
