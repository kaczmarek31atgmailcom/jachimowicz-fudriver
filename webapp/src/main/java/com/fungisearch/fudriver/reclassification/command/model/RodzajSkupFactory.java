package com.fungisearch.fudriver.reclassification.command.model;

import com.fungisearch.fudriver.reclassification.command.repository.RodzajSkupRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RodzajSkupFactory {
    private final RodzajSkupRepository rodzajSkupRepository;
    private final BeanValidator beanValidator;

    public RodzajSkupFactory(RodzajSkupRepository rodzajSkupRepository, BeanValidator beanValidator) {
        this.rodzajSkupRepository = rodzajSkupRepository;
        this.beanValidator = beanValidator;
    }

    public RodzajSkup findById(long id){
        RodzajSkup rodzajSkup = rodzajSkupRepository.get(id);
        if(rodzajSkup != null){
            rodzajSkup.rodzajSkupRepository = rodzajSkupRepository;
        }
        return rodzajSkup;
    }

    public RodzajSkup.RodzajSkupBuilder getBuilder(){
        return new RodzajSkup.RodzajSkupBuilder(rodzajSkupRepository);
    }

    public List<RodzajSkup> findAll(){
        List<RodzajSkup>  result = rodzajSkupRepository.getAllRodzajSkup();
        if(!result.isEmpty()){
            result.forEach(r-> r.rodzajSkupRepository = rodzajSkupRepository);
        }
        return result;
    }

    public RodzajSkupGrupa.RodzajSkupGrupaBuilder getRodzajSkupGrupaBuilder(){
        return new RodzajSkupGrupa.RodzajSkupGrupaBuilder(rodzajSkupRepository,beanValidator);
    }

    public RodzajSkup getDummy(){
        return new RodzajSkup.RodzajSkupBuilder(rodzajSkupRepository).buildDummy();
    }
}
