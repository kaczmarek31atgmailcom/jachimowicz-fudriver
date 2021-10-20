package com.fungisearch.fudriver.testTools.UT;

import com.fungisearch.fudriver.settings.command.model.Mycelium;
import com.fungisearch.fudriver.settings.command.repository.MyceliumRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

/**
 * Created by marcin on 12.04.17.
 */
public class MyceliumUTFactory {

    private final MyceliumRepository myceliumRepository;
    private final BeanValidator beanValidator;

    public MyceliumUTFactory(MyceliumRepository myceliumRepository, BeanValidator beanValidator) {
        this.myceliumRepository = myceliumRepository;
        this.beanValidator = beanValidator;
    }

    public Mycelium create(){
        Mycelium mycelium =  new Mycelium.MyceliumBuilder(myceliumRepository,beanValidator).name("mycelium").build();
        mycelium.setId(1L);
        return mycelium;
    }
}
