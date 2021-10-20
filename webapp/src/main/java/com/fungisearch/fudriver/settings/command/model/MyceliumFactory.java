package com.fungisearch.fudriver.settings.command.model;


import com.fungisearch.fudriver.settings.command.repository.MyceliumRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyceliumFactory {

    private final MyceliumRepository myceliumRepository;
    private final BeanValidator beanValidator;

    @Autowired
    public MyceliumFactory(MyceliumRepository myceliumRepository, BeanValidator beanValidator) {
        this.myceliumRepository = myceliumRepository;
        this.beanValidator = beanValidator;
    }

    public Mycelium.MyceliumBuilder getBuilder(){
        return new Mycelium.MyceliumBuilder(myceliumRepository,beanValidator);
    }

    public Mycelium find(Long id){
        Mycelium mycelium = null;
        if(id!=null) {
            mycelium = myceliumRepository.find(id);
            mycelium.myceliumRepository = myceliumRepository;
            mycelium.beanValidator = beanValidator;

        }
        return mycelium;
    }
}
