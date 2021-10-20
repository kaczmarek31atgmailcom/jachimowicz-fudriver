package com.fungisearch.fudriver.box.command.model;

import com.fungisearch.fudriver.box.command.repository.BoxRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoxFactory {
    private final BoxRepository boxRepository;
    private final BeanValidator beanValidator;

    @Autowired
    public BoxFactory(BoxRepository boxRepository, BeanValidator beanValidator) {
        this.boxRepository = boxRepository;
        this.beanValidator = beanValidator;
    }

    public Box.BoxBuilder getBuilder(){
        return new Box.BoxBuilder(boxRepository,beanValidator);
    }

    public Box find(Long id){
        Box box = null;
        if(id != null) {
            box = boxRepository.find(id);
            box.boxRepository = boxRepository;
            box.beanValidator = beanValidator;
        }
        return box;
    }
}
