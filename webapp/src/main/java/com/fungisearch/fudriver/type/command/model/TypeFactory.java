package com.fungisearch.fudriver.type.command.model;

import com.fungisearch.fudriver.payroll.wage.command.model.WageFactory;
import com.fungisearch.fudriver.type.command.repository.TypeRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TypeFactory {

    private final TypeRepository typeRepository;
    private final BeanValidator beanValidator;
    private final WageFactory wageFactory;

    @Autowired
    public TypeFactory(TypeRepository typeRepository, BeanValidator beanValidator, WageFactory wageFactory) {
        this.typeRepository = typeRepository;
        this.beanValidator = beanValidator;
        this.wageFactory = wageFactory;
    }

    public List<Type> findAll(){
        List<Type> types = typeRepository.findAll();
        for(Type type: types){
            type.typeRepository = typeRepository;
            type.beanValidator = beanValidator;
            type.wageFactory = wageFactory;
        }
    return types;
    }

    public Type findById(Long id){
        Type type = typeRepository.findById(id);
        if(type != null) {
            type.typeRepository = this.typeRepository;
            type.beanValidator = this.beanValidator;
            type.wageFactory = wageFactory;
        }
        return type;
    }

    public TypeGroup findTypeGroup(Long id){
        TypeGroup typeGroup = null;
        if(id != null) {
            typeGroup = typeRepository.findTypeGroup(id);
            typeGroup.typeRepository = typeRepository;
            typeGroup.beanValidator = beanValidator;
        }
        return typeGroup;
    }

    public TypeSize findTypeSize(Long id){
        TypeSize typeSize = null;
        if(id != null){
            typeSize = typeRepository.findTypeSize(id);
            typeSize.typeRepository = typeRepository;
            typeSize.beanValidator = beanValidator;
        }
    return typeSize;
    }

    public Type.TypeBuilder getBuilder(){
        return new Type.TypeBuilder(typeRepository,beanValidator,wageFactory);
    }

    public TypeGroup.TypeGroupBuilder getTypeGroupBuilder(){
        return new TypeGroup.TypeGroupBuilder(typeRepository,beanValidator);
    }

    public TypeSize.TypeSizeBuilder getTypeSizeBuilder(){return new TypeSize.TypeSizeBuilder(typeRepository,beanValidator);}
}
