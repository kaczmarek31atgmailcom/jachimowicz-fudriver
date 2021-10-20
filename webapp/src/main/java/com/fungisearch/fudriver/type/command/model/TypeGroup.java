package com.fungisearch.fudriver.type.command.model;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.type.command.repository.TypeRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;

@Entity
@Table(name="grupy_rodzaje")
public class TypeGroup extends BaseEntity {

    public transient TypeRepository typeRepository;
    public transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private boolean active;

    private TypeGroup(){}

    public TypeGroup(TypeRepository typeRepository, BeanValidator beanValidator){
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

    private void create() {
        beanValidator.validate(this);
        typeRepository.save(this);
    }

    public void delete(){
        this.active = false;
        beanValidator.validate(this);
    }

    public static class TypeGroupBuilder{
        private final TypeRepository typeRepository;
        private final BeanValidator beanValidator;
        private String name;

        public TypeGroupBuilder(TypeRepository typeRepository, BeanValidator beanValidator) {
            this.typeRepository = typeRepository;
            this.beanValidator = beanValidator;
        }

        public TypeGroupBuilder name(String name){
            this.name = name;
            return this;
        }

        public TypeGroup build(){
            TypeGroup typeGroup = new TypeGroup(typeRepository,beanValidator);
            typeGroup.name = this.name;
            typeGroup.active = true;
            typeGroup.create();
            return typeGroup;
        }
    }

}
