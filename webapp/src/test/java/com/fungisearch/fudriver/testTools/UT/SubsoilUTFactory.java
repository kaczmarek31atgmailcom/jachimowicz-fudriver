package com.fungisearch.fudriver.testTools.UT;

import com.fungisearch.fudriver.settings.command.model.Subsoil;
import com.fungisearch.fudriver.settings.command.repository.SubsoilRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

/**
 * Created by marcin on 12.04.17.
 */
public class SubsoilUTFactory {
    private final SubsoilRepository subsoilRepository;
    private final BeanValidator beanValidator;

    public SubsoilUTFactory(SubsoilRepository subsoilRepository, BeanValidator beanValidator) {
        this.subsoilRepository = subsoilRepository;
        this.beanValidator = beanValidator;
    }

    public Subsoil create(){
        Subsoil subsoil = new Subsoil.SubsoilBuilder(subsoilRepository,beanValidator)
                .name("subsoil")
                .build();
        subsoil.setId(1L);
        return subsoil;
    }
}
