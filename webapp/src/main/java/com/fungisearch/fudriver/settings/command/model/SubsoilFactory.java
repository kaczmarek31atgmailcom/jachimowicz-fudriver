package com.fungisearch.fudriver.settings.command.model;

import com.fungisearch.fudriver.settings.command.repository.SubsoilRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubsoilFactory {
    private final SubsoilRepository subsoilRepository;
    private final BeanValidator beanValidator;

    @Autowired
    public SubsoilFactory(SubsoilRepository subsoilRepository, BeanValidator beanValidator) {
        this.subsoilRepository = subsoilRepository;
        this.beanValidator = beanValidator;
    }

    public Subsoil.SubsoilBuilder getBuilder(){
        return new Subsoil.SubsoilBuilder(subsoilRepository,beanValidator);
    }

    public Subsoil find(long id){
        Subsoil subsoil = subsoilRepository.find(id);
        subsoil.subsoilRepository = subsoilRepository;
        subsoil.beanValidator = beanValidator;
        return subsoil;
    }
}
