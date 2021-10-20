package com.fungisearch.fudriver.wozek.command.model;

import com.fungisearch.fudriver.wozek.command.repository.TrolleyManRepository;
import org.springframework.stereotype.Service;

@Service
public class TrolleyManFactory {
    private final TrolleyManRepository trolleyManRepository;

    public TrolleyManFactory(TrolleyManRepository trolleyManRepository) {
        this.trolleyManRepository = trolleyManRepository;
    }

    public TrolleyMan.TrolleyManBuilder getBuilder(){
        return new TrolleyMan.TrolleyManBuilder(trolleyManRepository);
    }

    public TrolleyMan find(int id){
        TrolleyMan trolleyMan = trolleyManRepository.find(id);
        if(trolleyMan != null){
            trolleyMan.trolleyManRepository = trolleyManRepository;
        }
        return trolleyMan;
    }
}
