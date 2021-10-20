package com.fungisearch.fudriver.type.command.model;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.type.command.repository.TypeRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;

@Entity
@Table(name = "type_size")
public class TypeSize extends BaseEntity {
    public transient TypeRepository typeRepository;
    public transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private boolean active;

    private TypeSize(){}

    public TypeSize(TypeRepository typeRepository, BeanValidator beanValidator) {
        this.typeRepository = typeRepository;
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

    public void remove(){
        this.active = false;
        beanValidator.validate(this);
    }

    private void create(){
        beanValidator.validate(this);
        typeRepository.save(this);
    }

    public static class TypeSizeBuilder{
        private TypeRepository typeRepository;
        private BeanValidator beanValidator;
        private String name;

        public TypeSizeBuilder(TypeRepository typeRepository, BeanValidator beanValidator) {
            this.typeRepository = typeRepository;
            this.beanValidator = beanValidator;
        }

        public TypeSizeBuilder name(String name){
            this.name = name;
            return this;
        }

        public TypeSize build(){
            TypeSize typeSize = new TypeSize(typeRepository,beanValidator);
            typeSize.typeRepository = typeRepository;
            typeSize.beanValidator = beanValidator;
            typeSize.active = true;
            typeSize.name = name;
            typeSize.create();
            return typeSize;
        }

    }
}
